package com.example.steam.domain.notification;

import com.example.steam.domain.notification.model.NotiType;
import com.example.steam.domain.user.User;
import jakarta.persistence.*;
import kotlin.internal.NoInfer;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @ManyToOne(fetch = FetchType.LAZY)
    private Long userId;

    private String title;

    private String message;

    @Enumerated(EnumType.STRING)
    private NotiType type;

    // 읽음 표시
    private boolean isRead = false;

    private String createdAt = LocalDateTime.now().toString();

    // 알림 읽음
    public void markRead() {
        this.isRead = true;
    }

    public static Notification notify(Long userId, NotiType type, String title, String message, boolean isRead, String createdAt) {
        Notification notification = new Notification();
        notification.userId = userId;
        notification.type = type;
        notification.title = title;
        notification.message = message;
        notification.isRead = isRead;
        notification.createdAt = createdAt;
        return notification;
    }
}
