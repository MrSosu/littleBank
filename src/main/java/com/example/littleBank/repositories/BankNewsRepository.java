package com.example.littleBank.repositories;

import com.example.littleBank.entities.BankNews;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BankNewsRepository extends JpaRepository<BankNews, Long> {
}
