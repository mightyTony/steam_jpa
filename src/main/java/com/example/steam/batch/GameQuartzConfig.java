package com.example.steam.batch;

import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GameQuartzConfig {
    // ---------------주간 -----------------------

    @Bean
    public JobDetail weeklyRankingJobDetail() {
        return JobBuilder.newJob(WeeklyGameRankingQuartzJob.class)
                .withIdentity("weeklyRankingJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger weeklyRankingTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(weeklyRankingJobDetail())
                .withIdentity("weeklyRankingTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 3 ? * MON")) // 매주 월요일 3시
                .build();
    }

    // ---------------- 월간 ----------------------------------//

    @Bean
    public JobDetail monthlyRankingJobDetail() {
        return JobBuilder.newJob(MonthlyGameRankingQuartzJob.class)
                .withIdentity("monthlyRankingJob")
                .storeDurably()
                .build();
    }

    @Bean
    public Trigger monthlyRankingTrigger() {
        return TriggerBuilder.newTrigger()
                .forJob(monthlyRankingJobDetail())
                .withIdentity("monthlyRankingTrigger")
                .withSchedule(CronScheduleBuilder.cronSchedule("0 0 3 1 * ?")) // 매월 1일 3시
                .build();
    }
}
