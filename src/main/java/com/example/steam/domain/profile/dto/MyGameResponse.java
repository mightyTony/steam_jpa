package com.example.steam.domain.profile.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class MyGameResponse {
    private Long id;
    private Long gameId;
    private String name;
    private String developer;
    private String publisher;
    private String pictureUrl;

    @QueryProjection
    public MyGameResponse(Long id, Long gameId, String name, String developer, String publisher, String pictureUrl) {
        this.id = id;
        this.gameId = gameId;
        this.name = name;
        this.developer = developer;
        this.publisher = publisher;
        this.pictureUrl = pictureUrl;
    }
}
