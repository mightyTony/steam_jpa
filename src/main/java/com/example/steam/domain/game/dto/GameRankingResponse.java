package com.example.steam.domain.game.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@NoArgsConstructor
public class GameRankingResponse {
    private Long id;
    private String name;
    private String developer;
    private String publisher;
//    private String content;
    private int price;
    private int totalPrice;
    private String pictureUrl;
    private int sales;
    private int discount;
//    private  boolean onSale;
    private String releaseDate;
    private int likeCount;
    private int disLikeCount;

    @QueryProjection
    public GameRankingResponse(Long id, String name,
                               String developer, String publisher,
                               int price, int totalPrice,
                               String pictureUrl, int sales,
                               int discount, String releaseDate,
                               int likeCount, int disLikeCount) {
        this.id = id;
        this.name = name;
        this.developer = developer;
        this.publisher = publisher;
        this.price = price;
        this.totalPrice = totalPrice;
        this.pictureUrl = pictureUrl;
        this.sales = sales;
        this.discount = discount;
//        this.onSale = onSale;
        this.releaseDate = releaseDate;
        this.likeCount = likeCount;
        this.disLikeCount = disLikeCount;
    }
}
