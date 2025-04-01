package com.example.steam.domain.game.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "게임 정보 수정 요청 DTO")
public class GameUpdateRequest {
    @Schema(description = "게임 이름", example = "StarCraft II")
    private String name;

    @Schema(description = "개발사", example = "Blizzard")
    private String developer;

    @Schema(description = "배급사", example = "Blizzard")
    private String publisher;

    @Schema(description = "게임 설명", example = "신규 미션 추가")
    private String content;

    @Schema(description = "가격", example = "29900")
    private Integer price;

}
