package com.example.littleBank.controllers;

import com.example.littleBank.entities.BankNews;
import com.example.littleBank.services.BankNewsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/news")
public class BankNewsController {

    @Autowired
    private BankNewsService bankNewsService;

    @PostMapping("/create")
    public ResponseEntity<BankNews> createNews(@RequestBody BankNews bankNews) {
        return new ResponseEntity<>(bankNewsService.createBankNews(bankNews), HttpStatus.CREATED);
    }

}
