package com.example.littleBank.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String nome;
    @Column(nullable = false)
    private String cognome;
    @Column
    private LocalDate dataNascita;
    @Column
    private String indirizzo;
    @Column
    private String comune;
    @Column(nullable = false, unique = true)
    private String codiceFiscale;
    @Column(unique = true)
    private String telefono;
    @Column(nullable = false, unique = true)
    private String email;
    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "clientiConto")
    private List<Conto> contiUtente;


}
