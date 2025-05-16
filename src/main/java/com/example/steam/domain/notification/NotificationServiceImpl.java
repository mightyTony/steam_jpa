package com.example.steam.domain.notification;

import com.example.steam.domain.game.Game;
import com.example.steam.domain.notification.dto.NotificationDto;
import com.example.steam.domain.notification.model.NotiType;
import com.example.steam.domain.notification.query.NotificationRepository;
import com.example.steam.domain.profile.Profile;
import com.example.steam.domain.user.User;
import com.example.steam.domain.wish.query.WishRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cglib.core.Local;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.transaction.annotation.Propagation.REQUIRES_NEW;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {

    private final NotificationRepository notificationRepository;
    private final SseService sseService;
    private final WishRepository wishRepository;

    @Override
    public Long unreadNotificationsCount(User user) {
        return notificationRepository.countUnreadNotification(user);
    }

    @Override
    public List<NotificationDto> latestNotificationsTop10(User user) {
        return notificationRepository.latestNotificationsTop10(user);
    }

    @Override
    @Transactional(propagation = REQUIRES_NEW)
    public void sendSaleNotifications(Game game) {
        List<Long> userIds = wishRepository.findUsersByGame(game);

        List<Notification> notifications = new ArrayList<>();
        for (Long userId : userIds) {
            Notification notification = Notification.notify(
                    userId,
                    NotiType.SALE,
                    "찜한 게임이 할인 중이에요!",
                    game.getName() + "가 현재 " + game.getDiscount() + "% 할인 중 입니다.",
                    false,
                    LocalDateTime.now().toString()
            );
            notifications.add(notification);
        }

        notificationRepository.saveAll(notifications);
        notificationRepository.flush();
        log.info("[게임 할인 이벤트 알람 저장: {}건]", notifications.size());

        sseService.sendNotifications(notifications);
    }

    @Override
    @Transactional(propagation = REQUIRES_NEW)
    public void sendProfileCommentNotification(User writer, Profile profile) {
        User receiver = profile.getUser();

        if (!receiver.equals(writer)) {
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

    @Override
    @Transactional(propagation = REQUIRES_NEW)
    public void sendFriendNotifications(User receiver, User user) {
        Notification notification = Notification.notify(
                receiver.getId(),
                NotiType.FRIEND,
                "새로운 친구 요청",
                user.getNickname() + "님이 친구 요청을 보냈습니다.",
                false,
                LocalDateTime.now().toString()
        );

        notificationRepository.save(notification);
        sseService.sendNotification(receiver.getId(), notification);
    }


}
