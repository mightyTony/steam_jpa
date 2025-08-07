package com.example.steam.domain.notification;

import com.example.steam.domain.notification.dto.NotificationDto;
import com.example.steam.domain.user.User;
import com.example.steam.util.Response;
import com.example.steam.util.annotation.LoginUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;

@RequestMapping("/api/v1/notifications")
@Slf4j
@RestController
@RequiredArgsConstructor
@Tag(name = "알람", description = "알람 API")
public class NotificationController {
    private final NotificationService notificationService;
    private final SseService sseService;

    @Operation(
            summary = "안 읽은 알림 개수 조회",
            description = "유저가 클릭 하지 않은 알람 수를 조회 합니다."
    )
    // 안 읽은 알림 개수 조회
    @GetMapping("/count")
    @LoginUser
    public Response<Integer> getUnreadNotificationsCount(@AuthenticationPrincipal User user) {
        Long count = notificationService.unreadNotificationsCount(user);

        return Response.success(count.intValue());
    }


    // 최신 알림 10개 조회
    @Operation(summary = "최근 받은 알림 10개를 조회")
    @GetMapping("/latest")
    @LoginUser
    public Response<List<NotificationDto>> getLatestNotifications (@AuthenticationPrincipal User user) {
        List<NotificationDto> result = notificationService.latestNotificationsTop10(user);

        return Response.success(result);
    }

    // 알림 SSE 구독
    @Operation(summary = "알림 서비스 구독")
    @GetMapping(value = "/subscribe", produces = "text/event-stream")
    @LoginUser
    public SseEmitter subscribe(@RequestParam("token") String token) {
        return sseService.connect(token);
    }

    // 알림 읽음 처리
    @Operation(summary = "알림 읽음 처리")
    @PutMapping("/{id}/read")
    @LoginUser
    public Response<Void> updateReadStatus(@PathVariable("id") Long noticeId, @AuthenticationPrincipal User user) {
        notificationService.updateReadStatus(noticeId, user);

        return Response.success();
    }

    // 알림 모두 읽음 처리
    @Operation(summary = "모든 알림 읽음 처리")
    @PutMapping("/{id}/read-all")
    @LoginUser
    public Response<Void> updateAllNotification(@AuthenticationPrincipal User user,
                                                @PathVariable("id") Long userId) {
        notificationService.updateReadStatusAll(user, userId);

        return Response.success();
    }

    // 알림 삭제 처리
}
