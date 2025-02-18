package com.example.steam.domain.game.dto;

import com.example.steam.domain.game.Game;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class GameUpdateResponse {
    private String name;
    private String developer;
    private String publisher;
    private String content;
    private Integer price;
    private String pictureUrl;

    public GameUpdateResponse(Game game) {
        this.name = game.getName();
        this.developer = game.getDeveloper();
        this.publisher = game.getPublisher();
        this.content = game.getContent();
        this.price = game.getPrice();
        this.pictureUrl = game.getPictureUrl();
    }
}
