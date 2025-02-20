package com.example.steam.domain.game.query;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import software.amazon.awssdk.services.s3.endpoints.internal.Value;

import static com.example.steam.domain.game.QGame.game;

public class GamePredicate {

    // 판매중
    public static BooleanExpression isOnSale() {
        return game.onSale.isTrue();
    }

    // 제목 포함
    public static BooleanBuilder nameContain(String name) {
        BooleanBuilder builder = new BooleanBuilder();
        if(name != null && !name.isEmpty()) {
            builder.and(game.name.containsIgnoreCase(name));
        }
        return builder;
    }

    // 장르
    public static BooleanBuilder genreEquals(String genre) {
        BooleanBuilder builder = new BooleanBuilder();
        if(genre != null && !genre.isEmpty()) {
            builder.and(game.genres.any().genreName.eq(genre));
        }

        return builder;
    }

    // 가격
    public static BooleanBuilder priceBetween(Integer minPrice, Integer maxPrice) {
        BooleanBuilder builder = new BooleanBuilder();

        if(minPrice != null) {
            builder.and(game.price.goe(minPrice)); // greater of equals
        }

        if(maxPrice != null) {
            builder.and(game.price.loe(maxPrice));
        }

        return builder;
    }
}
