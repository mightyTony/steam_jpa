package com.example.steam.domain.profile.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "댓글 작성 요청 DTO")
public class CommentPostRequest {

    @Schema(description = "댓글 내용", example = "프로필 멋지네요!")
    @NotBlank(message = "댓글 내용을 입력해주세요.")
    private String content;
}
