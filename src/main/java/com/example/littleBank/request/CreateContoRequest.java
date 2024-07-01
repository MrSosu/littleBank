package com.example.littleBank.request;

import lombok.*;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateContoRequest {

    private Double cash;
    private Double costo;
    private List<Long> clienti;

}
