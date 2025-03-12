package com.example.steam.domain.order.dto;

import com.example.steam.domain.order.OrderItem;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class OrderItemResponse {
    private Long gameId;
    private String gameName;
    private int price;

    public static OrderItemResponse fromEntity(OrderItem orderItem) {
        return OrderItemResponse.builder()
                .gameId(orderItem.getGame().getId())
                .gameName(orderItem.getGame().getName())
                .price(orderItem.getPrice())
                .build();
    }
}
