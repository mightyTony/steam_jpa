package com.example.steam.domain.review.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class GameReviewReadResponse {
    private final Long reviewId;
    private final String username;
    private final String nickname;
    private final boolean recommend;
    private final Integer likeCount;
    private final String content;
    private final String createdAt;
    private final String updatedAt;

    @QueryProjection
    public GameReviewReadResponse(Long reviewId, String username, String nickname, boolean recommend, Integer likeCount, String content, String createdAt, String updatedAt) {
        this.reviewId = reviewId;
        this.username = username;
        this.nickname = nickname;
        this.recommend = recommend;
        this.likeCount = likeCount;
        this.content = content;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }
}
