package com.example.steam.domain.game.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class GameDiscountRequest {
    @Min(0)@Max(100)
    private int discount;
}
