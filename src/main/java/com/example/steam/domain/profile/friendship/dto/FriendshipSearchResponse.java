package com.example.steam.domain.profile.friendship.dto;

import com.example.steam.domain.profile.friendship.FriendStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FriendshipSearchResponse {
    private Long id;
    private Long senderId;
    private Long receiverId;
    private FriendStatus status;

    @QueryProjection
    public FriendshipSearchResponse(Long id, Long senderId, Long receiverId, FriendStatus status) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.status = status;
    }
}
