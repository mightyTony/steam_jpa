package com.example.steam.domain.game.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
@Getter
@NoArgsConstructor
@Schema(description = "게임 등록 요청 DTO", type = "object")
public class GameCreateRequest {

    @Schema(description = "게임 이름", example = "StarCraft")
    private String name;

    @Schema(description = "개발사", example = "Blizzard")
    private String developer;

    @Schema(description = "배급사", example = "Blizzard")
    private String publisher;

    @Schema(description = "게임 설명", example = "전략 시뮬레이션 게임")
    private String content;

    @Schema(description = "가격", example = "19900")
    private int price;

    @NotBlank(message = "출시일은 필수입니다.")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "출시일 형식은 yyyy-MM-dd 이어야 합니다.")
    @Schema(description = "출시일", example = "2024-04-01")
    private String releaseDate;

    @Schema(description = "장르 목록", example = "[\"Strategy\", \"Multiplayer\"]")
    private List<String> genres;

    @Builder
    public GameCreateRequest(String name, String developer, String publisher, String content, int price, String releaseDate, List<String> genres) {
        this.name = name;
        this.developer = developer;
        this.publisher = publisher;
        this.content = content;
        this.price = price;
        this.releaseDate = releaseDate;
        this.genres = genres;
    }
}
