package com.example.littleBank.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateContoResponse {

    private Long id_conto;
    private Double cash;
    private Double costo;
    private LocalDate dataConto;
    private List<Long> id_clienti;

}
