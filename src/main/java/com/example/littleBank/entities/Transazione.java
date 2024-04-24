package com.example.littleBank.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.time.LocalDateTime;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transazione {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Check(constraints = "cash > 0")
    private Double cash;
    @Column(nullable = false)
    private LocalDateTime timestamp;
    @ManyToOne
    private Conto mittente;
    @ManyToOne
    private Conto destinatario;


}
