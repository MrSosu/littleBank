package com.example.littleBank.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Conto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    @Check(constraints = "costo >= 0")
    private Double costo;
    @Column(nullable = false)
    @Check(constraints = "cash >= 0")
    private Double cash;
    @Column(nullable = false)
    private LocalDate dataConto;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "clienti_conti_utente",
            joinColumns = @JoinColumn(name = "cliente_id"),
            inverseJoinColumns = @JoinColumn(name = "conto_id")
    )
    private List<Cliente> clientiConto;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Conto conto = (Conto) o;
        return Objects.equals(id, conto.id) && Objects.equals(costo, conto.costo) && Objects.equals(cash, conto.cash) && Objects.equals(dataConto, conto.dataConto);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, costo, cash, dataConto);
    }
}
