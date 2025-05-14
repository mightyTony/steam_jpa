package com.example.steam.domain.notification.event;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.notification.Notification;
import com.example.steam.domain.notification.SseService;
import com.example.steam.domain.notification.model.NotiType;
import com.example.steam.domain.notification.query.NotificationRepository;
import com.example.steam.domain.user.User;
import com.example.steam.domain.wish.query.WishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import java.time.LocalDateTime;
import java.util.List;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationRepository notificationRepository;
    private final SseService sseService;
    private final WishRepository wishRepository;

    // 댓글 알람
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleCommentAlarm(CommentWriteEvent event) {
        User receiver = event.getProfile().getUser();
        User writer = event.getWriter();

        if(!receiver.equals(writer)) {

            Notification notification = Notification.notify(
                    receiver.getId(),
                    NotiType.PROFILE_COMMENT,
                    "내 프로필에 댓글이 달렸어요",
                    writer.getNickname() + "님이 프로필에 댓글을 남겼습니다.",
                    false,
                    LocalDateTime.now().toString()
            );

            notificationRepository.save(notification);
            sseService.sendNotification(receiver.getId(), notification);
        }
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleSaleAlarm(GameSaleEvent event) {
        log.info("[게임 할인 이벤트 알람 - {}", event.getGame());
        Game game = event.getGame();
        List<Long> userIds = wishRepository.findUsersByGame(game);


        List<Notification> notifications = userIds.stream()
                .map(user -> Notification.notify(
                user,
                NotiType.SALE,
                "찜한 게임이 할인 중이에요!",
                game.getName() + "가 현재" + game.getDiscount() + "% 할인 중 입니다.",
                false,
                LocalDateTime.now().toString()
        )).toList();

        try {
            notificationRepository.saveAll(notifications);
            log.info("[게임 할인 이벤트 알람 저장: {}건]", notifications.size());
        } catch (Exception e) {
            log.error("[게임 할인 이벤트 알람 저장 실패: {}", e.getMessage());
        }

        sseService.sendNotifications(notifications);

    }
}
