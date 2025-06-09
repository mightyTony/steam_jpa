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

public class MonthlyGameRankingJobConfig {

    private final JobRepository jobRepository;
    private final PlatformTransactionManager transactionManager;
    private final GameRankingTasklet gameRankingTasklet;

    @Bean(name = "monthlyGameRankingJob")
    public Job MonthlyGameRankingJob() {
        return new JobBuilder("monthlyGameRankingJob", jobRepository)
                .start(monthlyRankingStep())
                .build();
    }

    @Bean
    public Step monthlyRankingStep() {
        return new StepBuilder("monthlyRankingStep", jobRepository)
                .tasklet(((contribution, chunkContext) -> {
                    gameRankingTasklet.updateMonthlyGameRanking();
                    return RepeatStatus.FINISHED;
                }),transactionManager)
                .build();
    }
}
