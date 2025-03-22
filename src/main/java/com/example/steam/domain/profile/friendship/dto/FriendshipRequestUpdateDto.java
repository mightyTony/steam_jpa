package com.example.steam.domain.profile.friendship.dto;

import com.example.steam.domain.profile.friendship.FriendStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FriendshipRequestUpdateDto {
    private FriendStatus friendStatus;
}
