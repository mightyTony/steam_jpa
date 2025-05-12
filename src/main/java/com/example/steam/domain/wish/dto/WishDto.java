package com.example.steam.domain.wish.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class WishDto {
    private final Long id;
    private final Long gameId;
    private final String name;
    private final String developer;
    private final String publisher;
    private final String content;
    private final int price;
    private final int discount;
    private final int totalPrice;
    private final String pictureUrl;
    private final boolean onSale;

    @QueryProjection
    public WishDto(Long id, Long gameId, String name, String developer, String publisher, String content, int price, int discount, int totalPrice, String pictureUrl, boolean onSale) {
        this.id = id;
        this.gameId = gameId;
        this.name = name;
        this.developer = developer;
        this.publisher = publisher;
        this.content = content;
        this.price = price;
        this.discount = discount;
        this.totalPrice = totalPrice;
        this.pictureUrl = pictureUrl;
        this.onSale = onSale;
    }
}
