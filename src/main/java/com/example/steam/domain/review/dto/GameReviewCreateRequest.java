package com.example.steam.domain.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Schema(description = "게임 리뷰 작성 요청 DTO")
public class GameReviewCreateRequest {

    @NotNull(message = "게임 ID를 입력하세요")
    @Schema(description = "게임 ID", example = "1")
    private Long gameId;

    @NotNull(message = "추천 여부를 입력하세요. 추천 = true, 비추천 = false")
    @Schema(description = "리뷰 추천 여부", example = "true")
    private Boolean recommend;

    @NotBlank(message = "리뷰 내용을 작성해주세요")
    @Schema(description = "리뷰 내용", example = "정말 재미있는 게임이었어요!")
    private String content;
}
