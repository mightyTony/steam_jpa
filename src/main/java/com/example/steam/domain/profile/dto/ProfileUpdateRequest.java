package com.example.steam.domain.profile.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "프로필 상태메시지 수정 요청 DTO")
public class ProfileUpdateRequest {
    @Schema(description = "프로필 상태 메시지 (한줄 소개)", example = "게임 좋아하는 백엔드 개발자입니다.")
    @NotBlank(message = "상태 메시지는 비워둘 수 없습니다.")
    private String content;
    @QueryProjection
    public ProfileUpdateRequest(String content) {
        this.content = content;
    }
}
