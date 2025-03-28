package com.example.steam.domain.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GameReviewCreateResponse {
    private Long reviewId;
    private Long gameId;
    private String content;
    private boolean recommend;
    private boolean deleted;
    // LocalDateTime => String
    private String createdAt;
    private String lastModifiedAt;
    private String username;
    private String nickname;
    private String profileImageUrl;

    @QueryProjection
    public GameReviewCreateResponse(Long reviewId, Long gameId, String content, boolean recommend,
                                    boolean deleted, String createdAt, String lastModifiedAt,
                                    String username, String nickname, String profileImageUrl) {
        this.reviewId = reviewId;
        this.gameId = gameId;
        this.content = content;
        this.recommend = recommend;
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
