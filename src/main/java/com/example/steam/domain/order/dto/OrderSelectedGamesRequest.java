package com.example.steam.domain.order.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
@Schema(description = "선택된 게임 결제 요청 DTO")
public class OrderSelectedGamesRequest {
    @NotNull
    @NotEmpty(message = "선택된 게임 ID 목록은 비어 있을 수 없습니다.")
    @Schema(description = "선택한 게임 ID 리스트", example = "[1, 2, 3]")
    private List<Long> selectedGameIds;
}
