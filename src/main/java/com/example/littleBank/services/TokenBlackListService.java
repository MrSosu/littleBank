package com.example.littleBank.services;

import com.example.littleBank.entities.ScheduledBankNews;
import com.example.littleBank.entities.TokenBlackList;
import com.example.littleBank.jobs.TokenBlackListJob;
import com.example.littleBank.repositories.TokenBlackListRepository;
import jakarta.annotation.PostConstruct;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class TokenBlackListService {

    @Autowired
    private TokenBlackListRepository tokenBlackListRepository;
    @Autowired
    private Scheduler scheduler;
    private static final Object lock = new Object();

    public List<String> tokenNotValidFromClienteById(Long id_cliente) {
        return tokenBlackListRepository.getTokenBlackListFromClienteId(id_cliente)
                .stream()
                .map(TokenBlackList::getToken)
                .toList();
    }

    public void createTokenBlackList(TokenBlackList tokenBlackList) {
        tokenBlackList.setInsertTime(LocalDateTime.now());
        tokenBlackListRepository.saveAndFlush(tokenBlackList);
    }

    public List<TokenBlackList> getAll() {
        return tokenBlackListRepository.findAll();
    }

    public Boolean isTokenPresent(String token) {
        return tokenBlackListRepository.findAll().stream().map(TokenBlackList::getToken).toList().contains(token);
    }

    @PostConstruct
    public void startScheduledJob() throws SchedulerException {
        JobDetail jobDetail = buildJobDetail();
        Trigger trigger = buildJobTrigger(jobDetail, new Date());
        scheduler.scheduleJob(jobDetail, trigger);
    }

    private JobDetail buildJobDetail() {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("tokenBlackListService", this);

        return JobBuilder.newJob(TokenBlackListJob.class)
                .usingJobData(jobDataMap)
                .storeDurably()
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, Date targetDate) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .startAt(targetDate)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule()
                        .withIntervalInSeconds(5)
                        .repeatForever())
                .build();
    }

    public void executeScheduledTask() {
        synchronized (lock) {
            if (tokenBlackListRepository == null) return;
            if (!getAll().isEmpty()) {
                deleteTokens();
            }
        }
    }

    public void deleteTokens() {
        List<TokenBlackList> tokens = tokenBlackListRepository.findAll();
        if (tokens.isEmpty()) throw new NullPointerException();

        List<TokenBlackList> deleteTokens = tokens.stream()
                .filter(t -> Objects.nonNull(t.getInsertTime()) &&
                        Duration.between(t.getInsertTime(), LocalDateTime.now()).getSeconds() >= 20)
                .toList();

        tokenBlackListRepository.deleteAllInBatch(deleteTokens);
    }



}
