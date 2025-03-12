package com.example.steam.domain.order;

import com.example.steam.domain.order.dto.KakaoPayApprovalResponse;
import com.example.steam.domain.order.dto.KakaoPayReadyResponse;
import com.example.steam.domain.user.User;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@LoginUser
@RequestMapping("/api/v1/payment")
public class OrderController {

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
            @RequestBody List<Long> selectedGameIds) {
        KakaoPayReadyResponse response = orderService.readyPaymentSelected(user, selectedGameIds);
        return Response.success(response);
    }


    // 즉시 결제 준비
    @LoginUser
    @PostMapping("/ready/now")
    public Response<KakaoPayReadyResponse> payNow(
            @AuthenticationPrincipal User user,
            @RequestBody Long gameId) {
        KakaoPayReadyResponse response = orderService.readyPaymentNow(user, gameId);
        return Response.success(response);
    }


    // -- 결제 승인 --

    // 장바구니 전체 결제 승인
    @GetMapping("/success")
    public Response<KakaoPayApprovalResponse> approvePayment(@RequestParam("pg_token") String pg_token, @RequestParam("oid") Long orderId) {
        KakaoPayApprovalResponse response = orderService.approvePayment(orderId, pg_token);
        return Response.success(response);
    }


    // 장바구니 에서 선택한 게임만 결제 승인
    @GetMapping("/success/selected")
    public Response<KakaoPayApprovalResponse> approvePaymentSelected(
            @RequestParam String pg_token,
            @RequestParam Long orderId) {
        KakaoPayApprovalResponse response = orderService.approvePayment(orderId, pg_token);
        return Response.success(response);
    }

    // 즉시 결제 승인
    @GetMapping("/success/now")
    public Response<KakaoPayApprovalResponse> approvePaymentNow(
            @RequestParam String pg_token,
            @RequestParam Long orderId) {
        KakaoPayApprovalResponse response = orderService.approvePayment(orderId, pg_token);
        return Response.success(response);
    }

}
