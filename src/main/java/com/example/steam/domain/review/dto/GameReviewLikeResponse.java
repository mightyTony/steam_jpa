package com.example.steam.domain.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GameReviewLikeResponse {
    private final Long reviewId;
    private final boolean isLiked;
    private final Long likeCount;

    @Builder
    public GameReviewLikeResponse(Long reviewId, boolean isLiked, Long likeCount) {
        this.reviewId = reviewId;
        this.isLiked = isLiked;
        this.likeCount = likeCount;
    }
}
