package com.example.steam.domain.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GameReviewReadResponse {
    private final Long reviewId;
    private final String username;
    private final String nickname;
    private final String profileImageUrl;
    private final boolean recommend;
    private final Integer reviewlikeCount;
    private final String content;
    private final String createdAt;
    private final String updatedAt;

    @QueryProjection
    public GameReviewReadResponse(Long reviewId, String username, String nickname, String profileImageUrl, boolean recommend, Integer reviewlikeCount, String content, String createdAt, String updatedAt) {
        this.reviewId = reviewId;
        this.username = username;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.recommend = recommend;
        this.reviewlikeCount = reviewlikeCount;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
