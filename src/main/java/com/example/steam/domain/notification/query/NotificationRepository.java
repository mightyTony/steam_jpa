package com.example.steam.domain.notification.query;

import com.example.steam.domain.notification.Notification;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotificationRepository extends JpaRepository<Notification, Long>, NotificationRepositoryCustom {

//    void updateNotificationsByUserIdAndReadIsTrue(Long userId);
}
