package com.example.littleBank.jobs;

import com.example.littleBank.services.TokenBlackListService;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TokenBlackListJob implements Job {

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        TokenBlackListService tokenBlackListService = (TokenBlackListService) jobExecutionContext.getMergedJobDataMap().get("tokenBlackListService");
        tokenBlackListService.executeScheduledTask();
    }
}
