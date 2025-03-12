package com.example.steam.domain.order.dto;

import lombok.Data;

import java.util.List;

@Data
public class OrderReadyRequest {
    private Long userId;
    private List<Long> gameIds;
}
