package com.example.steam.domain.notification;

import com.example.steam.domain.notification.dto.NotificationDto;
import com.example.steam.domain.notification.query.NotificationRepository;
import com.example.steam.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SseService sseService;

    @Override
    public Long unreadNotificationsCount(User user) {
        return notificationRepository.countUnreadNotification(user);
    }

    @Override
    public List<NotificationDto> latestNotificationsTop10(User user) {
        return notificationRepository.latestNotificationsTop10(user);
    }



}
