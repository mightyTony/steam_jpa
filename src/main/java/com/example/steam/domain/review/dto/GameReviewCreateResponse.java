package com.example.steam.domain.review.dto;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.review.entity.GameReview;
import com.example.steam.domain.review.entity.ReviewType;
import com.example.steam.domain.user.User;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GameReviewCreateResponse {
    private Long reviewId;
    private Long gameId;
    private String content;
    private ReviewType reviewType;
    private boolean deleted;
    // LocalDateTime => String
    private String createdAt;
    private String lastModifiedAt;
    private String username;
    private String nickname;
    private String profileImageUrl;

    @QueryProjection
    public GameReviewCreateResponse(Long reviewId, Long gameId, String content, ReviewType reviewType,
                                    boolean deleted, String createdAt, String lastModifiedAt,
                                    String username, String nickname, String profileImageUrl) {
        this.reviewId = reviewId;
        this.gameId = gameId;
        this.content = content;
        this.reviewType = reviewType;
        this.deleted = deleted;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.username = username;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }

    //    public GameReviewCreateResponse create(GameReview review) {
//        this.id = review.getId();
//        this.content = review.getContent();
//        this.reviewType = review.getReviewType();
//        this.deleted = review.isDeleted();
//        this.createdAt = review.getCreatedAt().toString();
//        this.lastModifiedAt = review.getLastModifiedAt().toString();
//        this.nickname = review.getUser().getNickname();
//        this.profileImageUrl = review.getUser().getProfileImageUrl();
//    }
}
