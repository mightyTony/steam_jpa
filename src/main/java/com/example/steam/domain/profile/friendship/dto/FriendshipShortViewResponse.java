package com.example.steam.domain.profile.friendship.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

import java.util.List;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class FriendshipShortViewResponse {
    private Long totalCount;
    private List<FriendshipReadResponse> response;

    @QueryProjection
    public FriendshipShortViewResponse(Long totalCount, List<FriendshipReadResponse> response) {
        this.totalCount = totalCount;
        this.response = response;
    }
}
