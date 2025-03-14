package com.example.steam.domain.review.dto;

import com.example.steam.domain.review.entity.ReviewType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class GameReviewCreateRequest {

    @NotNull(message = "게임 ID를 입력하세요")
    private Long gameId;

    @NotNull(message = "추천/비추천 선택하세요")
    private ReviewType reviewType;

    @NotBlank(message = "리뷰 내용 써주세요")
    private String content;
}
