package com.example.littleBank.configuration;

import com.example.littleBank.entities.ScheduledBankNews;
import com.example.littleBank.services.ScheduledBankNewsService;
import org.quartz.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Date;

@Configuration
public class QuartzConfig {

    @Bean
    public SchedulerFactoryBean schedulerFactory() {
        SchedulerFactoryBean factoryBean = new SchedulerFactoryBean();
        // Configurazioni aggiuntive, se necessarie
        return factoryBean;
    }
}
