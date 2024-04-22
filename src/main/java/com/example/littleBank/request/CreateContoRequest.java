package com.example.littleBank.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CreateContoRequest {

    private Double cash;
    private Double costo;
    private List<Long> clienti;

}
