package com.example.steam.domain.order.dto;

import com.example.steam.domain.order.Order;
import lombok.Builder;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
@Builder
public class OrderHistoryResponse {
    private Long orderId;
    private int totalPrice;
    private String status;
    private String createdAt;
    private List<OrderItemResponse> orderItems;

    public static OrderHistoryResponse fromEntity(Order order) {
        return OrderHistoryResponse.builder()
                .orderId(order.getId())
                .totalPrice(order.getTotalPrice())
                .status(order.getStatus().name())
                .createdAt(order.getCreatedAt().toString())
                .orderItems(order.getOrderItems().stream()
                        .map(OrderItemResponse::fromEntity)
                        .collect(Collectors.toList()))
                .build();
    }
}

