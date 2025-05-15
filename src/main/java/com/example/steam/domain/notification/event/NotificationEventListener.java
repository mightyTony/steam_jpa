package com.example.steam.domain.notification.event;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.notification.Notification;
import com.example.steam.domain.notification.NotificationService;
import com.example.steam.domain.notification.SseService;
import com.example.steam.domain.notification.model.NotiType;
import com.example.steam.domain.notification.query.NotificationRepository;
import com.example.steam.domain.user.User;
import com.example.steam.domain.wish.query.WishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;

    // 댓글 알람
    @TransactionalEventListener
    public void handleCommentAlarm(CommentWriteEvent event) {
        notificationService.sendProfileCommentNotification(event.getWriter(), event.getProfile());
    }

    @TransactionalEventListener
    public void handleSaleAlarm(GameSaleEvent event) {
        log.info("[게임 할인 이벤트 알람 - {}", event.getGame());
        notificationService.sendSaleNotifications(event.getGame());
    }
}
