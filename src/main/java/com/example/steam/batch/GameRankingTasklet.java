package com.example.steam.batch;

import com.example.steam.domain.game.dto.GameRankingResponse;
import com.example.steam.domain.game.query.GameRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
@Component
@Slf4j
@RequiredArgsConstructor
public class GameRankingTasklet {
    private final GameRepository gameRepository;
    private final RedisTemplate<String, Object> redisTemplate;


    // 주간 랭킹 - 매주 월요일 오전 3시 실행
//    @Scheduled(cron = "0 0 3 * * MON", zone = "Asia/Seoul")
    public void updateWeeklyGameRanking() {
        log.info("[LOG] [BATCH] - 주간 게임 판매량 랭킹 캐싱");
        List<GameRankingResponse> result = gameRepository.findTopGamesBySales(LocalDateTime.now().minusWeeks(1));

        // 레디스 저장
        redisTemplate.opsForValue().set("game:ranking:weekly", result, Duration.ofDays(7));
        log.info("[LOG] [BATCH] - 주간 게임 판매량 랭킹 캐싱 완료 ({}개)", result.size());
    }

    // 월간 랭킹 - 매월 1일 오전 3시 실행
//    @Scheduled(cron = "0 0 3 1 * *", zone = "Asia/Seoul")
    public void updateMonthlyGameRanking() {
        log.info("[LOG] [BATCH] - 월간 게임 판매량 랭킹 캐");
        List<GameRankingResponse> result = gameRepository.findTopGamesBySales(LocalDateTime.now().minusMonths(1));

        // 레디스 저장
        redisTemplate.opsForValue().set("game:ranking:monthly", result, Duration.ofDays(31));
        log.info("[LOG] [BATCH] - 월간 게임 판매량 랭킹 캐싱 완료 ({}개)", result.size());
    }

}
