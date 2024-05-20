package com.example.littleBank.controllers;

import com.example.littleBank.entities.BankNews;
import com.example.littleBank.exceptions.BankNewsNotFoundException;
import com.example.littleBank.request.ScheduledBankNewsRequest;
import com.example.littleBank.services.BankNewsService;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/news")
public class BankNewsController {

    @Autowired
    private BankNewsService bankNewsService;

    @GetMapping("/get/{id}")
    public ResponseEntity<BankNews> getBankNewsById(@PathVariable Long id) throws BankNewsNotFoundException {
        BankNews bankNews = bankNewsService.getBankNewsById(id);
        return new ResponseEntity<>(bankNews, HttpStatus.OK);
    }

    @GetMapping("/all")
    public ResponseEntity<List<BankNews>> getAllBankNews() {
        return new ResponseEntity<>(bankNewsService.getAllNews(), HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<BankNews> createNews(@RequestBody BankNews bankNews) {
        return new ResponseEntity<>(bankNewsService.createBankNews(bankNews), HttpStatus.CREATED);
    }



}
