package com.example.steam.domain.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "게임 리뷰 작성 응답 DTO")
public class GameReviewCreateResponse {

    @Schema(description = "리뷰 ID", example = "101")
    private Long reviewId;

    @Schema(description = "게임 ID", example = "1")
    private Long gameId;

    @Schema(description = "리뷰 내용", example = "명작입니다")
    private String content;

    @Schema(description = "추천 여부", example = "true")
    private boolean recommend;

    @Schema(description = "삭제 여부", example = "false")
    private boolean deleted;

    @Schema(description = "작성일", example = "2025-03-31T13:45:00")
    private String createdAt;

    @Schema(description = "수정일", example = "2025-03-31T13:55:00")
    private String lastModifiedAt;

    @Schema(description = "작성자 ID", example = "user123")
    private String username;

    @Schema(description = "작성자 닉네임", example = "겜돌이")
    private String nickname;

    @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/profile.jpg")
    private String profileImageUrl;

//    private Long reviewId;
//    private Long gameId;
//    private String content;
//    private boolean recommend;
//    private boolean deleted;
//    // LocalDateTime => String
//    private String createdAt;
//    private String lastModifiedAt;
//    private String username;
//    private String nickname;
//    private String profileImageUrl;

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

}
