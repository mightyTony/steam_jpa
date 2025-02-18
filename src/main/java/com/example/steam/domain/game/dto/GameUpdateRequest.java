package com.example.steam.domain.game.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
// TODO validation
public class GameUpdateRequest {
    private String name;
    private String developer;
    private String publisher;
    private String content;
    private Integer price;
    private String pictureUrl;
}
