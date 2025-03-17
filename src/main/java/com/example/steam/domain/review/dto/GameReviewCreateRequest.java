package com.example.steam.domain.review.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class GameReviewCreateRequest {

    @NotNull(message = "게임 ID를 입력하세요")
    private Long gameId;

    @NotNull(message = "추천/비추천 값 입력해주세요. 추천 = 1, 비추천 = 0")
    private boolean recommend;

    @NotBlank(message = "리뷰 내용 써주세요")
    private String content;
}
