package com.example.steam.domain.notification;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
@Slf4j
public class SseService {

    private final Map<Long, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter connect(Long userId) {
//        if (emitters.containsKey(userId)) {
//            emitters.remove(userId).complete();
//        }

        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE);

        emitters.put(userId, emitter);

        emitter.onCompletion(() -> emitters.remove(userId));
        emitter.onTimeout(() -> emitters.remove(userId));
        emitter.onError(e -> emitters.remove(userId));

//        log.info("sse 연결- {} ",userId);
        try {
            emitter.send(SseEmitter.event()
                    .name("sse")
                    .data("연결되었습니다. EventStream Created. [userId="+userId+"]"));
        } catch (Exception e) {
            log.warn("[LOG] SSE 전송 실패: userId: {}, error: {}", userId, e.toString());
            emitters.remove(userId);
        }
        return emitter;
    }

    public void sendNotification(Long userId, Notification notification) {
        SseEmitter emitter = emitters.get(userId);

        if(emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("notification")
                        .data(notification));
            } catch (Exception e) {
                emitters.remove(userId);
            }
        }
    }

    // 할인 알림 용
    public void sendNotifications(List<Notification> notifications) {
        log.info("[LOG] 알림 전송 - 전체 대상 {} ", notifications.size());
        for (Notification notification : notifications) {
            Long userId = notification.getUserId();
            SseEmitter emitter = emitters.get(userId);

            if (!emitters.containsKey(userId)) {
                log.warn("[LOG] Emitter 없음 - userId={}", userId);
            }

            if (emitter != null) {
                try {
                    emitter.send(SseEmitter.event()
                            .name("notification")
                            .data(notification));
                } catch (Exception e) {
                    log.warn("[LOG] SSE 전송 실패 - userId: {}, 이유: {}", userId, e.toString());
                    emitters.remove(userId);
                }
            }
        }
    }
}
