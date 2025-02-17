package com.example.steam.domain.game.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

// TODO validation
@Getter
@NoArgsConstructor
public class GameCreateRequest {

    private String name;
    private String developer;
    private String publisher;
    private String content;
    private int price;

    @NotBlank(message = "출시일은 필수입니다")
    @Pattern(regexp = "^\\d{4}-\\d{2}-\\d{2}$", message = "출시일 형식은 yyyy-MM-dd 이어야 합니다.")
    private String releaseDate;
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
