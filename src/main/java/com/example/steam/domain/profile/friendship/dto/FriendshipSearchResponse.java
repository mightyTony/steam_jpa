package com.example.steam.domain.profile.friendship.dto;

import com.example.steam.domain.profile.friendship.FriendStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FriendshipSearchResponse {
    private final Long id;
    private final Long senderId;
    private final Long receiverId;
    private final String receiverName;
    private final FriendStatus status;

    @QueryProjection
    public FriendshipSearchResponse(Long id, Long senderId, Long receiverId, String receiverName, FriendStatus status) {
        this.id = id;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.receiverName = receiverName;
        this.status = status;
    }
}
