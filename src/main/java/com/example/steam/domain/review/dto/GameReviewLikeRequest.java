package com.example.steam.domain.review.dto;

import lombok.Getter;

@Getter
public class GameReviewLikeRequest {
    private Long like;
    private Long reviewId;

}
