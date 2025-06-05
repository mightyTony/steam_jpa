package com.example.steam.domain.notification;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.notification.dto.NotificationDto;
import com.example.steam.domain.profile.Profile;
import com.example.steam.domain.user.User;

import java.util.List;

public interface NotificationService {
    Long unreadNotificationsCount(User user);

    List<NotificationDto> latestNotificationsTop10(User user);

    void sendSaleNotifications(Game game);

    void sendProfileCommentNotification(User writer, Profile profile);

    void sendFriendNotifications(User receiver, User user);

    void updateReadStatus(Long noticeId, User user);

//    void updateReadStatusAll(User user);
}
