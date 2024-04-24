package com.example.littleBank.repositories;

import com.example.littleBank.entities.Transazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TransazioneRepository extends JpaRepository<Transazione, Long> {

    @Query(value = "SELECT * FROM transazione WHERE mittente_id = :idConto OR destinatario_id = :idConto", nativeQuery = true)
    List<Transazione> getTransazioniByContoId(@Param("idConto") Long idConto);

}
