package com.example.steam.domain.profile.friendship.dto;

import com.example.steam.domain.profile.friendship.FriendStatus;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "친구 요청 상태 변경 DTO")
public class FriendshipRequestUpdateDto {
    @Schema(description = "요청 상태 (ACCEPTED 또는 REJECTED)", example = "ACCEPTED")
    private FriendStatus friendStatus;
}
