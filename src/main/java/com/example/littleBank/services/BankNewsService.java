package com.example.littleBank.services;

import com.example.littleBank.entities.BankNews;
import com.example.littleBank.exceptions.BankNewsNotFoundException;
import com.example.littleBank.repositories.BankNewsRepository;
import com.example.littleBank.request.ScheduledBankNewsRequest;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BankNewsService implements Job {

    @Autowired
    private BankNewsRepository bankNewsRepository;
    @Autowired
    private Scheduler scheduler;

    public BankNews getBankNewsById(Long id) throws BankNewsNotFoundException {
        Optional<BankNews> optionalBankNews = bankNewsRepository.findById(id);
        if (optionalBankNews.isEmpty()) throw new BankNewsNotFoundException();
        return optionalBankNews.get();
    }

    public List<BankNews> getAllNews() {
        return bankNewsRepository.findAll();
    }

    public BankNews createBankNews(BankNews bankNews) {
        bankNewsRepository.saveAndFlush(bankNews);
        return bankNews;
    }

    public ScheduledBankNewsRequest createScheduledBankNews(ScheduledBankNewsRequest request) throws SchedulerException {
        BankNews bankNews = BankNews.builder().title(request.getTitle()).body(request.getBody()).build();
        JobDetail jobDetail = buildJobDetail(bankNews);
        Trigger trigger = buildJobTrigger(jobDetail, request.getTargetDate());
        scheduler.scheduleJob(jobDetail, trigger);
        return request;
    }

    private JobDetail buildJobDetail(BankNews bankNews) {
        JobDataMap jobDataMap = new JobDataMap();
        jobDataMap.put("entity", bankNews);
        return JobBuilder.newJob()
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
        BankNews bankNews = (BankNews) jobDataMap.get("entity");
        createBankNews(bankNews);
        System.out.println("News inserita con successo!");
    }
}
