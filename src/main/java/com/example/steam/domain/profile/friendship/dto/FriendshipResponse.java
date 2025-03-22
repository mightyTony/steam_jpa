package com.example.steam.domain.profile.friendship.dto;

import com.example.steam.domain.profile.friendship.FriendStatus;
import com.example.steam.domain.profile.friendship.Friendship;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FriendshipResponse {
    private Long requesterId;
    private String requesterUsername;
    private String requesterNickname;
    private String requesterProfileImageUrl;
    private FriendStatus status;

    public FriendshipResponse (Friendship friendship) {
        this.requesterId = friendship.getId();
        this.requesterUsername = friendship.getSender().getUsername();
        this.requesterNickname = friendship.getSender().getNickname();
        this.requesterProfileImageUrl = friendship.getSender().getProfileImageUrl();
        this.status = friendship.getStatus();
    }

    public FriendshipResponse() {
    }

    public static FriendshipResponse toDto(Friendship friendship) {
        FriendshipResponse response = new FriendshipResponse();
        response.requesterId = friendship.getId();
        response.requesterUsername = friendship.getSender().getUsername();
        response.requesterNickname = friendship.getSender().getNickname();
        response.requesterProfileImageUrl = friendship.getSender().getProfileImageUrl();
        response.status = friendship.getStatus();
        return response;
    }
}
