package com.example.littleBank.services;

import com.example.littleBank.entities.BankNews;
import com.example.littleBank.repositories.BankNewsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BankNewsService {

    @Autowired
    private BankNewsRepository bankNewsRepository;

    public BankNews createBankNews(BankNews bankNews) {
        bankNewsRepository.saveAndFlush(bankNews);
        return bankNews;
    }

}
