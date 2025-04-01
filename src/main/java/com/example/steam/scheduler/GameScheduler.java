package com.example.steam.scheduler;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class GameScheduler {

    @Scheduled(fixedRate = 5000)
    public void cronJob() {
        System.out.println("Cron running at 5s: " + LocalDateTime.now());
    }
}
