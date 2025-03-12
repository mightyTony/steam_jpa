package com.example.steam.domain.order;

import com.example.steam.domain.order.dto.KakaoPayApprovalResponse;
import com.example.steam.domain.order.dto.KakaoPayReadyResponse;
import com.example.steam.domain.order.dto.OrderHistoryResponse;
import com.example.steam.domain.user.User;

import java.util.List;

public interface OrderService {

    KakaoPayReadyResponse readyPayment(User user);

    KakaoPayReadyResponse readyPaymentSelected(User user, List<Long> selectedGameIds);

    KakaoPayReadyResponse readyPaymentNow(User user, Long gameId);

    KakaoPayApprovalResponse approvePayment(Long orderId, String pgToken);

    void cancelOrder(Long orderId);

    void failOrder(Long orderId);

    List<OrderHistoryResponse> getOrderHistory(User user);
}