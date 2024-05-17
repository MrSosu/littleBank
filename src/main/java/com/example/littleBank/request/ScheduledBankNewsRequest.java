package com.example.littleBank.request;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ScheduledBankNewsRequest {

    private String title;
    private String body;
    private Date targetDate;

}
