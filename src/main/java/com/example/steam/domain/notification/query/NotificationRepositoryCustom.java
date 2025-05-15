package com.example.steam.domain.notification.query;

import com.example.steam.domain.notification.dto.NotificationDto;
import com.example.steam.domain.user.User;

import java.util.List;

public interface NotificationRepositoryCustom {
    Long countUnreadNotification(User user);

    List<NotificationDto> latestNotificationsTop10(User user);
}
