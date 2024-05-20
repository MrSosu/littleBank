package com.example.littleBank.controllers;

import com.example.littleBank.entities.ScheduledBankNews;
import com.example.littleBank.request.ScheduledBankNewsRequest;
import com.example.littleBank.services.ScheduledBankNewsService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/scheduled_news")
public class ScheduledBankNewsController {

    @Autowired
    private ScheduledBankNewsService scheduledBankNewsService;

    @PostMapping("/create")
    public ResponseEntity<ScheduledBankNewsRequest> createScheduledNews(@RequestBody ScheduledBankNewsRequest request) throws SchedulerException {
        return new ResponseEntity<>(scheduledBankNewsService.createScheduledBankNews(request), HttpStatus.CREATED);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<ScheduledBankNewsRequest> updateScheduledNews(@PathVariable Long id, @RequestBody ScheduledBankNewsRequest request) throws SchedulerException {
        return new ResponseEntity<>(scheduledBankNewsService.updateScheduledNews(id, request), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteScheduledNewsById(@PathVariable Long id) throws SchedulerException {
        scheduledBankNewsService.deleteScheduledNewsById(id);
    }

}
