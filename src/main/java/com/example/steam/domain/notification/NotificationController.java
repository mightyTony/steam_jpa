package com.example.steam.domain.notification;

import com.example.steam.domain.notification.dto.NotificationDto;
import com.example.steam.domain.user.User;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.LoginUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RequestMapping("/api/v1/notifications")
@Slf4j
@RestController
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;
    private final SseService sseService;

    // todo index (userId, read)
    // 안 읽은 알림 개수 조회
    @GetMapping("/count")
    @LoginUser
    public Response<Integer> getUnreadNotificationsCount(@AuthenticationPrincipal User user) {
        Long count = notificationService.unreadNotificationsCount(user);

        return Response.success(count.intValue());
    }


    // 최신 알림 10개 조회
    @GetMapping("/latest")
    @LoginUser
    public Response<List<NotificationDto>> getLatestNotifications (@AuthenticationPrincipal User user) {
        List<NotificationDto> result = notificationService.latestNotificationsTop10(user);

        return Response.success(result);
    }

    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    @LoginUser
    public SseEmitter subscribe(@AuthenticationPrincipal User user) {
        return sseService.connect(user.getId());
    }
}
