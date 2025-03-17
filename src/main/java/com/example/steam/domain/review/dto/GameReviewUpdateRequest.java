package com.example.steam.domain.review.dto;

import lombok.Getter;

@Getter
public class GameReviewUpdateRequest {
    private Boolean recommend;
    private String content;
}
