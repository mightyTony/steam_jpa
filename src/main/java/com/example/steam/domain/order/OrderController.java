package com.example.steam.domain.order;

import com.example.steam.domain.order.dto.KakaoPayApprovalResponse;
import com.example.steam.domain.order.dto.KakaoPayReadyResponse;
import com.example.steam.domain.order.dto.OrderHistoryResponse;
import com.example.steam.domain.order.dto.OrderSelectedGamesRequest;
import com.example.steam.domain.order.query.OrderRepository;
import com.example.steam.domain.user.User;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.LoginUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@LoginUser
@RequestMapping("/api/v1/payment")
public class OrderController {
    private final OrderRepository orderRepository;

    private final OrderService orderService;

    // 장바구니 전체 결제 준비
    @LoginUser
    @PostMapping("/ready")
    public Response<KakaoPayReadyResponse> payReady(
            @AuthenticationPrincipal User user) {
        KakaoPayReadyResponse kakaoPayReadyResponse = orderService.readyPayment(user);
        return Response.success(kakaoPayReadyResponse);
    }

    // 장바구니 에서 선택한 게임만 결제 준비
    @LoginUser
    @PostMapping("/ready/selected")
    public Response<KakaoPayReadyResponse> payReadySelected(
            @AuthenticationPrincipal User user,
            @RequestBody OrderSelectedGamesRequest request) {

        log.info("Received selectedGameIds: {}", request.getSelectedGameIds());
        KakaoPayReadyResponse response = orderService.readyPaymentSelected(user, request.getSelectedGameIds());
        return Response.success(response);
    }


    // 즉시 결제 준비
    @LoginUser
    @PostMapping("/ready/now")
    public Response<KakaoPayReadyResponse> payNow(
            @AuthenticationPrincipal User user,
            @RequestParam("game_id") Long gameId) {
        KakaoPayReadyResponse response = orderService.readyPaymentNow(user, gameId);
        return Response.success(response);
    }

    // 결제 내역 조회
    @LoginUser
    @GetMapping("/history")
    public Response<List<OrderHistoryResponse>> getOrderHistory(@AuthenticationPrincipal User user) {
        log.info("Controller - [history api]");
        List<OrderHistoryResponse> history = orderService.getOrderHistory(user);
        return Response.success(history);
    }

    // -- 결제 결과 --

    // 결제 승인
    @GetMapping("/success")
    public Response<KakaoPayApprovalResponse> approvePayment(@RequestParam("pg_token") String pg_token, @RequestParam("oid") Long orderId, HttpServletRequest request) {
        KakaoPayApprovalResponse response = orderService.approvePayment(orderId, pg_token);
        return Response.success(response);
    }

    // 결제 취소
    @GetMapping("/cancel")
    public Response<String> cancelPayment(@RequestParam("oid") Long orderId) {
        orderService.cancelOrder(orderId);

        return Response.error(HttpStatus.BAD_REQUEST.toString(), "결제가 취소되었습니다");
    }

    // 결제 실패
    @GetMapping("/fail")
    public Response<String> failPayment(@RequestParam("oid") Long orderId) {
        orderService.failOrder(orderId);
        return Response.error(HttpStatus.BAD_REQUEST.toString(), "결제가 실패하였습니다.");
    }

}
