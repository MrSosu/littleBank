package com.example.littleBank.request;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransazioneRequest {

    private Long idContoMittente;
    private Long idContoDestinatario;
    private Double cash;

}
