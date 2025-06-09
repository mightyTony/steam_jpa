package com.example.steam.batch;

import com.example.steam.exception.BatchJobException;
import com.example.steam.exception.ErrorCode;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WeeklyGameRankingQuartzJob implements org.quartz.Job {
    private final JobLauncher jobLauncher;
    @Qualifier("weeklyGameRankingJob")
    private final Job weeklyGameRankingJob;

    public WeeklyGameRankingQuartzJob(JobLauncher jobLauncher, @Qualifier("monthlyGameRankingJob")Job weeklyGameRankingJob) {
        this.jobLauncher = jobLauncher;
        this.weeklyGameRankingJob = weeklyGameRankingJob;
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        try {
            JobParameters params = new JobParametersBuilder()
                    .addLong("timestamp", System.currentTimeMillis())
                    .toJobParameters();
            jobLauncher.run(weeklyGameRankingJob, params);
        } catch (JobInstanceAlreadyCompleteException e) {
            throw new BatchJobException(ErrorCode.BATCH_ALREADY_COMPLETE);
        } catch (JobExecutionAlreadyRunningException e) {
            throw new BatchJobException(ErrorCode.BATCH_ALREADY_RUNNING);
        } catch (JobParametersInvalidException e) {
            throw new BatchJobException(ErrorCode.BATCH_INVALID_PARAMETER);
        } catch (JobRestartException e) {
            throw new BatchJobException(ErrorCode.BATCH_RESTART_FAILED);
        }
    }


}
