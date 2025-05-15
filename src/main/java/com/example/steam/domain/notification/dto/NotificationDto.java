package com.example.steam.domain.notification.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationDto {
    private final Long id;
    private final String title;
    private final String message;
    private final boolean isRead;
    private final String createdAt;

    @QueryProjection
    public NotificationDto(Long id, String title, String message, boolean isRead, String createdAt) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
}
