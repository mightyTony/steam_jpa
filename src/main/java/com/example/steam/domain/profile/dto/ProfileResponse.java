package com.example.steam.domain.profile.dto;

import com.example.steam.domain.profile.Profile;
import com.example.steam.domain.profile.friendship.FriendStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileResponse {
    @Schema(description = "유저 ID", example = "100")
    private Long userId;

    @Schema(description = "닉네임", example = "steam_dev")
    private String nickname;

    @Schema(description = "상태 메시지", example = "게임 좋아하는 개발자입니다.")
    private String content;

    @Schema(description = "프로필 이미지 URL", example = "https://cdn.example.com/images/profile.jpg")
    private String profileImageUrl;

    @Schema(description = "관계 상태", example = "ACCEPTED")
    private FriendStatus friendStatus;

    @QueryProjection
    public ProfileResponse(Long userId, String content, String nickname, String profileImageUrl, FriendStatus status) {
        this.userId = userId;
        this.content = content;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.friendStatus = status;
    }

    @QueryProjection
    public ProfileResponse(Long userId, String content, String nickname, String profileImageUrl) {
        this.userId = userId;
        this.content = content;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
        this.friendStatus = null;
    }

    public static ProfileResponse update(Profile profile) {
        ProfileResponse response = new ProfileResponse();
        response.userId = profile.getUser().getId();
        response.content = profile.getContent();
        response.nickname = profile.getUser().getNickname();
        response.profileImageUrl = profile.getUser().getProfileImageUrl();
        return response;
    }
}
