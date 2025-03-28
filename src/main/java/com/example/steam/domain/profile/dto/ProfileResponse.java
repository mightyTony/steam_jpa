package com.example.steam.domain.profile.dto;

import com.example.steam.domain.profile.Profile;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileResponse {
    private final Long userId;
    private final String content;
    private final String profileImageUrl;

    @QueryProjection
    public ProfileResponse(Long userId, String content, String profileImageUrl) {
        this.userId = userId;
        this.content = content;
        this.profileImageUrl = profileImageUrl;
    }

// N+1
//    public ProfileResponse(Profile profile) {
//        this.userId = profile.getUser().getId();
//        this.content = profile.getContent();
//        this.profileImageUrl = profile.getUser().getProfileImageUrl();
//    }
}
