package com.example.steam.domain.notification.query;

import com.example.steam.domain.notification.QNotification;
import com.example.steam.domain.notification.dto.NotificationDto;
import com.example.steam.domain.notification.dto.QNotificationDto;
import com.example.steam.domain.user.User;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;

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
                .where( notification.userId.eq(user.getId()),
                        notification.isRead.isFalse()
                )
                .fetch();
    }
}
