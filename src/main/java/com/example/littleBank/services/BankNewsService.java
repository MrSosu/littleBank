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
public class BankNewsService {

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


}
