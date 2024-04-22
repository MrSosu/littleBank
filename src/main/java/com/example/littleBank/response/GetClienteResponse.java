package com.example.littleBank.response;

import com.example.littleBank.entities.Conto;
import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToMany;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class GetClienteResponse {

    private Long id;
    private String nome;
    private String cognome;
    private LocalDate dataNascita;
    private String indirizzo;
    private String comune;
    private String codiceFiscale;
    private String telefono;
    private String email;
    private List<Long> id_conti_utente;

}
