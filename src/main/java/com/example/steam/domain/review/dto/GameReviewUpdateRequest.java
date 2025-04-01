package com.example.steam.domain.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

@Getter
@Schema(description = "게임 리뷰 수정 요청 DTO")
public class GameReviewUpdateRequest {
    @Schema(description = "추천 여부 변경", example = "false")
    private Boolean recommend;

    @Schema(description = "리뷰 내용 수정", example = "업데이트 이후 밸런스가 좀 아쉽네요")
    private String content;
}
