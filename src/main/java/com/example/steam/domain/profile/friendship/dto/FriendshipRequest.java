package com.example.steam.domain.profile.friendship.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "친구 요청 생성 DTO")
public class FriendshipRequest {
    @NotNull(message = "수신자 ID는 필수입니다.")
    @Schema(description = "친구 요청 받을 유저 ID", example = "42")
    private Long receiverId;
}
