package com.example.steam.domain.notification.event;

import com.example.steam.domain.notification.NotificationService;
import com.example.steam.domain.profile.Profile;
import com.example.steam.domain.profile.query.ProfileRepository;
import com.example.steam.domain.user.User;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@Slf4j
@RequiredArgsConstructor
public class NotificationEventListener {
    private final NotificationService notificationService;
    private final ProfileRepository profileRepository;

    // 댓글 알람
    @TransactionalEventListener
    public void handleCommentAlarm(CommentWriteEvent event) {
        notificationService.sendProfileCommentNotification(event.getWriter(), event.getProfile());
    }

    // 게임 세일 알람
    @TransactionalEventListener
    public void handleSaleAlarm(GameSaleEvent event) {
        log.info("[LOG] [게임 할인 이벤트 알람 - {}", event.getGame().getName());
        notificationService.sendSaleNotifications(event.getGame());
    }

    // 프로필 생성 이벤트
    @TransactionalEventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleProfileMake(UserJoinEvent event) {
        User user = event.getUser();
        Profile profile = Profile.builder()
                .user(user)
                .content("")
                .build();
        profileRepository.save(profile);
    }

    // 친구 요청 알람
    @TransactionalEventListener
    public void handleFriendAlarm(FriendEventNotification eventNotification) {
        log.info("[LOG] [친구 요청 알람] 보낸이 : {}, 받는 이 : {}", eventNotification.getUser().getNickname(), eventNotification.getReceiver().getNickname());
        notificationService.sendFriendNotifications(eventNotification.getReceiver(), eventNotification.getUser());
    }
}
