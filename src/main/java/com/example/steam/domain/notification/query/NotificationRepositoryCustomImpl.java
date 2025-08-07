package com.example.steam.domain.notification.query;

import com.example.steam.domain.notification.Notification;
import com.example.steam.domain.notification.QNotification;
import com.example.steam.domain.notification.dto.NotificationDto;
import com.example.steam.domain.notification.dto.QNotificationDto;
import com.example.steam.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class NotificationRepositoryCustomImpl implements NotificationRepositoryCustom {
    private final JPAQueryFactory queryFactory;
    private final QNotification notification = QNotification.notification;

    @Override
    public Long countUnreadNotification(User user) {
        return queryFactory
                .select(notification.count())
                .from(notification)
                .where(
                        notification.userId.eq(user.getId()),
                        notification.isRead.isFalse()
                )
                .fetchOne();
    }

    @Override
    public List<NotificationDto> latestNotificationsTop10(User user) {
        return queryFactory
                .select(new QNotificationDto(
                        notification.id,
                        notification.title,
                        notification.message,
                        notification.isRead,
                        notification.createdAt
                ))
                .from(notification)
                .where(notification.userId.eq(user.getId())
                        //notification.isRead.isFalse()
                )
                .orderBy(notification.createdAt.desc())
                .limit(10)
                .fetch();
    }

    @Override
    public Optional<Notification> findByIdAndUser(Long noticeId, User user) {

        return Optional.ofNullable(queryFactory
                .selectFrom(notification)
                .where(notification.id.eq(noticeId)
                        .and(notification.userId.eq(user.getId()))
                )
                .fetchOne());
    }

    @Override
    public Optional<List<Notification>> findAllByUser(User user, Long userId) {
        return Optional.ofNullable(queryFactory
                .selectFrom(notification)
                .where(notification.userId.eq(userId))
                .fetch());
    }
}
