package com.example.steam.domain.order.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderHistoryResponse {
    private Long gameId;
    private String gameName;
    private Long orderId;
    private int totalPrice;
    private String status;
    private String createdAt;
//    private List<OrderItemResponse> orderItems;

//    @QueryProjection
//    public static OrderHistoryResponse fromEntity(Order order, OrderItem orderItem) {
//        return OrderHistoryResponse.builder()
//                .orderId(order.getId())
//                .totalPrice(order.getTotalPrice())
//                .status(order.getStatus().name())
//                .createdAt(order.getCreatedAt().toString())
//                .gameId(orderItem.getGame().getId())
//                .gameName(orderItem.getGame().getName())
////                .orderItems(order.getOrderItems().stream()
////                        .map(OrderItemResponse::fromEntity)
////                        .collect(Collectors.toList()))
//                .build();
//    }
    @QueryProjection
    public OrderHistoryResponse(Long gameId, String gameName, Long orderId, int totalPrice, String status, String createdAt) {
        this.gameId = gameId;
        this.gameName = gameName;
        this.orderId = orderId;
        this.totalPrice = totalPrice;
        this.status = status;
        this.createdAt = createdAt;
    }
}

