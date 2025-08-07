package com.example.steam.domain.notification.query;

import com.example.steam.domain.notification.Notification;
import com.example.steam.domain.notification.dto.NotificationDto;
import com.example.steam.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface NotificationRepositoryCustom {
    Long countUnreadNotification(User user);

    List<NotificationDto> latestNotificationsTop10(User user);

    Optional<Notification> findByIdAndUser(Long noticeId, User user);

    Optional<List<Notification>> findAllByUser(User user, Long userId);
}
