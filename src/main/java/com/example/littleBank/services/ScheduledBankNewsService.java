package com.example.littleBank.services;

import com.example.littleBank.entities.BankNews;
import com.example.littleBank.entities.ScheduledBankNews;
import com.example.littleBank.repositories.ScheduledBankNewsRepository;
import com.example.littleBank.request.ScheduledBankNewsRequest;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

@Service
public class ScheduledBankNewsService implements Job {

    @Autowired
    private ScheduledBankNewsRepository scheduledBankNewsRepository;
    @Autowired
    private Scheduler scheduler;
    @Autowired
    private BankNewsService bankNewsService;

    public ScheduledBankNews getScheduledNewsById(Long id) {
        Optional<ScheduledBankNews> optionalScheduledBankNews = scheduledBankNewsRepository.findById(id);
        if (optionalScheduledBankNews.isEmpty()) throw new IllegalArgumentException("non esiste la news schedulata richiesta!");
        return optionalScheduledBankNews.get();
    }

    public ScheduledBankNewsRequest createScheduledBankNews(ScheduledBankNewsRequest request) throws SchedulerException {
        ScheduledBankNews scheduledBankNews = ScheduledBankNews
                .builder()
                .title(request.getTitle())
                .body(request.getBody())
                .publishTime(request.getTargetDate())
                .build();
        scheduledBankNewsRepository.saveAndFlush(scheduledBankNews);
        JobDetail jobDetail = buildJobDetail(scheduledBankNews);
        Trigger trigger = buildJobTrigger(jobDetail, request.getTargetDate());
        scheduler.scheduleJob(jobDetail, trigger);
        return request;
    }

    private JobDetail buildJobDetail(ScheduledBankNews bankNews) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("entity", bankNews);
        jobDataMap.put("id", bankNews.getId());
        return JobBuilder.newJob(ScheduledBankNewsService.class)
                .withIdentity(String.valueOf(bankNews.getId()), "bankNews")
                .storeDurably()
                .setJobData(jobDataMap)
                .build();
    }

    private Trigger buildJobTrigger(JobDetail jobDetail, Date targetDate) {
        return TriggerBuilder.newTrigger()
                .forJob(jobDetail)
                .startAt(targetDate)
                .withSchedule(SimpleScheduleBuilder.simpleSchedule())
                .build();
    }

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap jobDataMap = jobExecutionContext.getMergedJobDataMap();
        ScheduledBankNews scheduledBankNews = (ScheduledBankNews) jobDataMap.get("entity");
        try {
            deleteScheduledNewsById(scheduledBankNews.getId());
        } catch (SchedulerException e) {
            throw new RuntimeException(e);
        }
        BankNews bankNews = BankNews
                .builder()
                .title(scheduledBankNews.getTitle())
                .body(scheduledBankNews.getBody())
                .build();
        bankNewsService.createBankNews(bankNews);
        System.out.println("News inserita con successo!");
    }

    public ScheduledBankNewsRequest updateScheduledNews(Long id, ScheduledBankNewsRequest request) throws SchedulerException {
        deleteScheduledNewsById(id);
        return createScheduledBankNews(request);
    }

    public void deleteScheduledNewsById(Long id) throws SchedulerException {
        JobKey jobKey = new JobKey(String.valueOf(id), "bankNews");
        scheduler.deleteJob(jobKey);
        scheduledBankNewsRepository.deleteById(id);
    }

}
