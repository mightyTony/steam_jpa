package com.example.steam.domain.game.dto;

import com.example.steam.domain.game.Game;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GameResponse {
    private Long id;
    private String name;
    private String developer;
    private String publisher;
    private int price;
    private int totalPrice;
    private String pictureUrl;
    private boolean onSale;
    private String releaseDate;
    private List<String> genres;

    public GameResponse (Game game) {
        this.id = game.getId();
        this.name = game.getName();
        this.developer = game.getDeveloper();
        this.publisher = game.getPublisher();
        this.price = game.getPrice();
        this.totalPrice = game.getTotalPrice();
        this.pictureUrl = game.getPictureUrl();
        this.onSale = game.isOnSale();
        this.releaseDate = game.getReleaseDate();
        this.genres = game.getGenres().stream()
                .map(genres -> genres.getGenreName())
                .collect(Collectors.toList());
    }
}
