package com.example.steam.domain.game.dto;

import com.example.steam.domain.game.Game;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class GameDetailResponse {
    private Long id;
    private String name;
    private String developer;
    private String publisher;
    private String content;
    private int price;
    private String pictureUrl;
    private int sales;  // 판매량
    private Integer discount; // 할인률 %
    private int totalPrice;
    private boolean onSale;
    private String releaseDate;
    private List<String> genres;

    // entity -> response dto
    public GameDetailResponse(Game game) {
        this.id = game.getId();
        this.name = game.getName();
        this.developer = game.getDeveloper();
        this.publisher = game.getPublisher();
        this.content = game.getContent();
        this.price = game.getPrice();
        this.pictureUrl = game.getPictureUrl();
        this.sales = game.getSales();
        this.discount = game.getDiscount();
        this.totalPrice = game.getTotalPrice();
        this.onSale = game.isOnSale();
        this.releaseDate = game.getReleaseDate();
        this.genres = game.getGenres().stream()
                .map(genre -> genre.getGenreName())
                .collect(Collectors.toList());  // 게임의 장르 리스트 추가
    }

    @QueryProjection
    public GameDetailResponse(Long id, String name, String developer, String publisher, String content, int price, String pictureUrl, int sales, Integer discount, int totalPrice, boolean onSale, String releaseDate, List<String> genres) {
        this.id = id;
        this.name = name;
        this.developer = developer;
        this.publisher = publisher;
        this.content = content;
        this.price = price;
        this.pictureUrl = pictureUrl;
        this.sales = sales;
        this.discount = discount;
        this.totalPrice = totalPrice;
        this.onSale = onSale;
        this.releaseDate = releaseDate;
        this.genres = genres;
    }
}
