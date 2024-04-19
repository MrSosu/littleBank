package com.example.littleBank.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Finanziamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Check(constraints = "cash > 0")
    private Double cash;
    @Column(nullable = false)
    @Check(constraints = "tasso >= 0")
    private Double tasso;
    @Column(nullable = false)
    @Check(constraints = "durata_mesi > 0")
    private Integer durataMesi;
    @ManyToOne
    private Cliente cliente;

}
