package com.example.steam.domain.order;

import com.example.steam.domain.order.dto.KakaoPayApprovalResponse;
import com.example.steam.domain.order.dto.KakaoPayReadyResponse;
import com.example.steam.domain.order.dto.OrderHistoryResponse;
import com.example.steam.domain.order.dto.OrderSelectedGamesRequest;
import com.example.steam.domain.order.query.OrderRepository;
import com.example.steam.domain.user.User;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
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
@Tag(name = "주문", description = "게임 주문 관련 API 입니다. 로그인이 필요 합니다.")
public class OrderController {
    private final OrderRepository orderRepository;

    private final OrderService orderService;

    @Operation(summary = "장바구니 전체 결제 요청", description = "장바구니에 담긴 전체 게임을 결제 요청합니다.")
    @LoginUser
    @PostMapping("/ready")
    public Response<KakaoPayReadyResponse> payReady(
            @AuthenticationPrincipal User user) {
        log.info("[장바구니 전체 결제 요청] - user : {}", user.getUsername());
        KakaoPayReadyResponse kakaoPayReadyResponse = orderService.readyPayment(user);
        return Response.success(kakaoPayReadyResponse);
    }

    @Operation(summary = "선택 게임 결제 요청", description = "장바구니에서 선택한 게임만 결제합니다.")
    @LoginUser
    @PostMapping("/ready/selected")
    public Response<KakaoPayReadyResponse> payReadySelected(@AuthenticationPrincipal User user,
                                                            @Valid @RequestBody OrderSelectedGamesRequest request) {

        log.info("Received selectedGameIds: {}", request.getSelectedGameIds());
        KakaoPayReadyResponse response = orderService.readyPaymentSelected(user, request.getSelectedGameIds());
        return Response.success(response);
    }


    // 즉시 결제 준비
    @Operation(summary = "즉시 결제 요청", description = "게임 상세 페이지에서 바로 결제 요청을 합니다.")
    @LoginUser
    @PostMapping("/ready/now")
    public Response<KakaoPayReadyResponse> payNow(
                                                                   @AuthenticationPrincipal User user,
            @Parameter(description = "결제할 게임 ID", example = "5") @RequestParam("game_id") Long gameId) {
        log.info("[즉시 결제 요청] - user : {}, game_id {} ", user.getUsername(), gameId);
        KakaoPayReadyResponse response = orderService.readyPaymentNow(user, gameId);
        return Response.success(response);
    }

    // 결제 내역 조회
    @Operation(summary = "결제 내역 조회", description = "로그인한 사용자의 결제 내역을 조회합니다.")
    @LoginUser
    @GetMapping("/history")
    public Response<List<OrderHistoryResponse>> getOrderHistory(@AuthenticationPrincipal User user) {
        log.info("[결제 내역 조회] - user : {}", user.getUsername());
        List<OrderHistoryResponse> history = orderService.getOrderHistory(user);
        return Response.success(history);
    }

    // -- 결제 결과 --

    // 결제 승인
    @Operation(summary = "결제 승인", description = "카카오페이로부터 결제 성공 시 승인 처리합니다.")
    @GetMapping("/success")
    public Response<KakaoPayApprovalResponse> approvePayment(@RequestParam("pg_token") String pg_token,
                                                             @RequestParam("oid") Long orderId,
                                                             HttpServletRequest request) {
        log.info("[결제 승인] - oid: {}", orderId);
        KakaoPayApprovalResponse response = orderService.approvePayment(orderId, pg_token);
        return Response.success(response);
    }

    // 결제 취소
    @Operation(summary = "결제 취소", description = "결제 중 사용자가 취소한 경우 처리합니다.")
    @GetMapping("/cancel")
    public Response<String> cancelPayment(@RequestParam("oid") Long orderId) {
        log.info("[결제 취소] - oid: {}", orderId);
        orderService.cancelOrder(orderId);
        return Response.error(HttpStatus.BAD_REQUEST.toString(), "결제가 취소되었습니다");
    }

    // 결제 실패
    @Operation(summary = "결제 실패", description = "결제 중 오류가 발생한 경우 처리합니다.")
    @GetMapping("/fail")
    public Response<String> failPayment(@RequestParam("oid") Long orderId) {
        log.info("[결제 실패] - oid: {}", orderId);
        orderService.failOrder(orderId);
        return Response.error(HttpStatus.BAD_REQUEST.toString(), "결제가 실패하였습니다.");
    }

}
