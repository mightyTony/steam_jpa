package com.example.steam.domain.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
@Schema(description = "게임 할인 적용 요청 DTO")
public class GameDiscountRequest {

    @Min(0)
    @Max(100)
    @Schema(description = "할인율 (0~100)", example = "20")
    private int discount;
}