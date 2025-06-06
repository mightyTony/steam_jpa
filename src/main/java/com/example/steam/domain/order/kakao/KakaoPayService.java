package com.example.steam.domain.order.kakao;

import com.example.steam.domain.cart.Cart;
import com.example.steam.domain.cart.query.CartRepository;
import com.example.steam.domain.game.Game;
import com.example.steam.domain.game.query.GameRepository;
import com.example.steam.domain.profile.mygame.MyGame;
import com.example.steam.domain.profile.query.MyGameRepository;
import com.example.steam.domain.order.Order;
import com.example.steam.domain.order.OrderItem;
import com.example.steam.domain.order.OrderItemRepository;
import com.example.steam.domain.order.OrderService;
import com.example.steam.domain.order.dto.KakaoApproveRequest;
import com.example.steam.domain.order.dto.KakaoPayApprovalResponse;
import com.example.steam.domain.order.dto.KakaoPayReadyResponse;
import com.example.steam.domain.order.dto.OrderHistoryResponse;
import com.example.steam.domain.order.query.OrderRepository;
import com.example.steam.domain.user.User;
import com.example.steam.exception.ErrorCode;
import com.example.steam.exception.SteamException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class KakaoPayService implements OrderService {

    @Value("${kakaopay.secret-key}")
    private String secretKey;
    @Value("${kakaopay.cid}")
    private String cid;
    @Value("${kakaopay.url.ready}")
    private String KakaoReadyUrl;
    @Value("${kakaopay.url.approval}")
    private String approvalUrl;
    @Value("${kakaopay.url.cancel}")
    private String cancelUrl;
    @Value("${kakaopay.url.fail}")
    private String failUrl;
    @Value("${front.url}")
    private String serverUrl;

    private final RestTemplate restTemplate;
    private final CartRepository cartRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final GameRepository gameRepository;
    private final MyGameRepository myGameRepository;

    // 카카오 요구 헤더
    private HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "SECRET_KEY " + secretKey);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    private void checkAlreadyBuyGame(User user, List<Game> games) {
        List<Game> alreadyBuyGames = new ArrayList<>();

        for (Game game : games) {
            if(myGameRepository.existsByUserAndGame(user, game)) {
                alreadyBuyGames.add(game);
            }
        }

        if(!alreadyBuyGames.isEmpty()) {
            throw new SteamException(ErrorCode.ALREADY_BUY_GAME);
        }
    }


    // 장바구니 전체 결제 준비
    @Override
    @Transactional
    public KakaoPayReadyResponse readyPayment(User user) {
        // 장바구니에서 게임 가져오기
        List<Cart> cartItems = cartRepository.findByUser(user);
        if(cartItems.isEmpty()) {
            throw new SteamException(ErrorCode.NOT_FOUND_GAME_IN_CART);
        }

        List<Game> games = cartItems.stream().map(Cart::getGame).toList();

        // 이미 구매 한 게임 인지 체크
        checkAlreadyBuyGame(user, games);
        int totalPrice = games.stream().mapToInt(Game::getTotalPrice).sum();

        return createKakaoPayRequest(user, games, totalPrice);
    }

    // 장바구니에서 선택한 게임만 결제 준비
    @Override
    @Transactional
    public KakaoPayReadyResponse readyPaymentSelected(User user, List<Long> selectedGameIds) {
        List<Game> games = gameRepository.findAllById(selectedGameIds);
        if (games.isEmpty()) {
            throw new SteamException(ErrorCode.NOT_FOUND_GAME);
        }

        // 이미 구매 한 게임 인지 체크
        checkAlreadyBuyGame(user, games);

        int totalPrice = games.stream().mapToInt(Game::getTotalPrice).sum();
        return createKakaoPayRequest(user, games, totalPrice);
    }

    // 바로결제
    @Override
    @Transactional
    public KakaoPayReadyResponse readyPaymentNow(User user, Long gameId) {
        Game game = gameRepository.findById(gameId)
                .orElseThrow(() -> new SteamException(ErrorCode.NOT_FOUND_GAME));

        // 이미 구매 한 게임 인지 체크
        checkAlreadyBuyGame(user, List.of(game));

        return createKakaoPayRequest(user, List.of(game), game.getTotalPrice());
    }

    // 공통 결제 준비 로직
    private KakaoPayReadyResponse createKakaoPayRequest(User user, List<Game> games, int totalAmount) throws SteamException {
        // 주문 생성 및 저장
        Order order = Order.builder()
                .user(user)
                .totalPrice(totalAmount)
                .build();
        orderRepository.save(order);

        List<OrderItem> orderItems = games.stream()
                .map(game -> new OrderItem(order, game, game.getTotalPrice()))
                .toList();
        orderItemRepository.saveAll(orderItems);

        // 주문 이름
        String itemName;
        if(games.size() > 1){
            itemName = games.get(0).getName() + " 외 " + (games.size()-1) + "개";
        } else {
            itemName = games.get(0).getName();
        }

        // 카카오페이 API 요청 데이터 생성
        KakaoPayReadyRequest requestBody = KakaoPayReadyRequest.builder()
                .cid(cid)
                .partnerOrderId(order.getId().toString())
                .partnerUserId(user.getId().toString())
                .itemName(itemName)
                .quantity(games.size())
                .totalAmount(totalAmount)
                .taxFreeAmount(0)
                .approvalUrl(serverUrl + "/api/v1/payment/success?oid=" + order.getId())  // 성공시 리다이렉트
                .cancelUrl(serverUrl + "/api/v1/payment/cancel?oid=" + order.getId())  // 취소시
                .failUrl(serverUrl + "/api/v1/payment/fail?oid=" + order.getId())  // 실패시
                .build();

        // 카카오페이 결제 요청
        HttpEntity<KakaoPayReadyRequest> requestEntity = new HttpEntity<>(requestBody, getHeaders());
        ResponseEntity<KakaoPayReadyResponse> responseEntity = restTemplate.postForEntity(
                KakaoReadyUrl, requestEntity, KakaoPayReadyResponse.class);

        // 응답 처리 및 주문 업데이트
        KakaoPayReadyResponse response = responseEntity.getBody();

        if(response.getTid().isBlank() || response.getTid().isEmpty()) {
            throw new SteamException(ErrorCode.NOT_FOUND_ORDER_TID);
        }
        order.addTid(response.getTid());
        orderRepository.save(order);

        return response;
    }

    // 결제 승인
    @Transactional
    @Override
    public KakaoPayApprovalResponse approvePayment(Long orderId, String pgToken) {
        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new SteamException(ErrorCode.ORDER_NOT_FOUND));

        // 카카오페이 승인 요청 데이터
        KakaoApproveRequest request = KakaoApproveRequest.builder()
                .cid(cid)
                .tid(order.getTid())
                .partnerOrderId(order.getId().toString())
                .partnerUserId(order.getUser().getId().toString())
                .pgToken(pgToken)
                .build();

        HttpEntity<KakaoApproveRequest> requestEntity = new HttpEntity<>(request, getHeaders());
        ResponseEntity<KakaoPayApprovalResponse> responseEntity = restTemplate.postForEntity(
                approvalUrl, requestEntity, KakaoPayApprovalResponse.class);

        // 결제 실패시
        if (responseEntity.getStatusCode().is4xxClientError() || responseEntity.getStatusCode().is5xxServerError()) {
            log.warn("[LOG] [approvePayment] KakaoPayApproval failed");
            log.warn("[LOG] [approvePayment] KakaoPayApproval failed - status : {}", responseEntity.getStatusCode());
            log.warn("[LOG] [approvePayment] KakaoPayApproval failed - getBody : {}", responseEntity.getBody());
            order.failed();
            orderRepository.save(order);
            throw new SteamException(ErrorCode.ORDER_FAILED);
        }

        KakaoPayApprovalResponse response = responseEntity.getBody();

        // 결제 완료 처리
        order.completeOrder();
        orderRepository.save(order);

        // 마이 게임에 게임 등록
        enrollGameToMyGame(order);

        // 장바구니에서 결제된 게임 삭제
        removeGamesFromCart(order);

        // 게임 판매량 카운트 ++
        increaseGameSalesCount(order);

        return response;
    }

    private void increaseGameSalesCount(Order order) {
        List<OrderItem> items = order.getOrderItems();
        for (OrderItem item : items) {
            Game game = item.getGame();
            game.increaseSales();
        }
    }

    @Override
    @Transactional
    public void cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new SteamException(ErrorCode.ORDER_NOT_FOUND));


        order.cancel();
        orderRepository.save(order);

        log.info("[LOG] [결제 취소] - orderId : {}", order.getId());
    }

    @Override
    @Transactional
    public void failOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new SteamException(ErrorCode.ORDER_NOT_FOUND));

        order.failed();
        log.warn("[LOG] [결제 실패] - orderId : {}", order.getId());
        orderRepository.save(order);
    }

    // 결제 내역 조회
    @Override
    @Transactional(readOnly = true)
    public List<OrderHistoryResponse> getOrderHistory(User user) {
        List<Order> orders = orderRepository.findOrdersWithItemsByUser(user);

        List<OrderHistoryResponse> historyList = new ArrayList<>();

        for (Order order : orders) {
            historyList.add(OrderHistoryResponse.fromEntity(order));
        }

        return historyList;
    }

    // 내 게임에 등록
    private void enrollGameToMyGame(Order order) {
        List<OrderItem> orderItems = order.getOrderItems();

        List<MyGame> myGames = orderItems.stream()
                .map(orderItem -> MyGame.builder()
                        .user(order.getUser())
                        .game(orderItem.getGame())
                        .build()
                ).collect(Collectors.toList());

        myGameRepository.saveAll(myGames);
    }

    // 장바구니 삭제
    private void removeGamesFromCart(Order order) {
        cartRepository.deleteByUserAndGameIdIn(
                order.getUser(),
                order.getOrderItems().stream()
                        .map(orderItem -> orderItem.getGame().getId()).toList());
    }
}
