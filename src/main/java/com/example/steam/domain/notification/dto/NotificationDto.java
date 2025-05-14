package com.example.steam.domain.notification.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class NotificationDto {
    private Long id;
    private String title;
    private String message;
    private boolean isRead;
    private String createdAt;

    @QueryProjection
    public NotificationDto(Long id, String title, String message, boolean isRead, String createdAt) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.isRead = isRead;
        this.createdAt = createdAt;
    }
}
