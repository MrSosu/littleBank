package com.example.littleBank.repositories;

import com.example.littleBank.entities.ScheduledBankNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduledBankNewsRepository extends JpaRepository<ScheduledBankNews, Long> {
}
