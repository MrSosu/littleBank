package com.example.littleBank.response;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransazioneResponse {

    private Long idContoMittente;
    private Long idContoDestinatario;
    private LocalDateTime timestamp;
    private Double cash;

}
