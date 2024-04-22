package com.example.littleBank.repositories;

import com.example.littleBank.entities.Finanziamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FinanziamentoRepository extends JpaRepository<Finanziamento, Long>  {
}
