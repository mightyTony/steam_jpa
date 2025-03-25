package com.example.steam.domain.profile.dto;

import com.example.steam.domain.profile.Profile;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class ProfileResponse {
    private Long userId;
    private String content;
    private String profileImageUrl;

    @QueryProjection
    public ProfileResponse(Long userId, String content, String profileImageUrl) {
        this.userId = userId;
        this.content = content;
        this.profileImageUrl = profileImageUrl;
    }

    // Fixme
    public static ProfileResponse update(Profile profile) {
        ProfileResponse response = new ProfileResponse();
        response.userId = profile.getUser().getId();
        response.content = profile.getContent();
        response.profileImageUrl = profile.getUser().getProfileImageUrl();
        return response;
    }
}
