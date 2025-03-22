package com.example.steam.domain.profile.friendship.dto;

import com.example.steam.domain.profile.friendship.Friendship;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FriendshipReadResponse {
    private final Long userId;
    private final String username;
    private final String nickname;
    private final String profileImageUrl;

    @QueryProjection
    public FriendshipReadResponse(Long userId, String username, String nickname, String profileImageUrl) {
        this.userId = userId;
        this.username = username;
        this.nickname = nickname;
        this.profileImageUrl = profileImageUrl;
    }
}
