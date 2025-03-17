package com.example.steam.domain.review.dto;

import com.example.steam.domain.review.entity.GameReview;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Builder;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GameReviewUpdateResponse {
    private Long reviewId;
    private Long userId;
    private Long gameId;
    private Boolean recommend;
    private String content;
    private Boolean deleted;

    @Builder
    public GameReviewUpdateResponse(Long reviewId, Long userId, Long gameId,
                                    Boolean recommend, String content, Boolean deleted) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.gameId = gameId;
        this.recommend = recommend;
        this.content = content;
        this.deleted = deleted;
    }

    public static GameReviewUpdateResponse fromEntity (GameReview review) {
        return GameReviewUpdateResponse.builder()
                .reviewId(review.getId())
                .userId(review.getUser().getId())
                .gameId(review.getGame().getId())
                .recommend(review.getRecommend())
                .content(review.getContent())
                .deleted(review.isDeleted())
                .build();
    }
}
