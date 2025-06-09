package com.example.steam.batch;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@Slf4j

public class WeeklyGameRankingJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final GameRankingTasklet gameRankingTasklet;

    @Bean(name = "weeklyGameRankingJob")
    public Job weeklyGameRankingJob() {
        return new JobBuilder("weeklyGameRankingJob", jobRepository)
                .start(weeklyRankingStep())
                .build();
    }

    @Bean
    public Step weeklyRankingStep() {
        return new StepBuilder("weeklyRankingStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    gameRankingTasklet.updateWeeklyGameRanking();
                    return RepeatStatus.FINISHED;
                }), transactionManager)
                .build();
    }

}
