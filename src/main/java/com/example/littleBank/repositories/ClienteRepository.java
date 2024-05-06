package com.example.littleBank.repositories;

import com.example.littleBank.entities.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    @Query(value = "SELECT * FROM cliente c WHERE c.email = :email", nativeQuery = true)
    Cliente findClienteByEmail(@Param("email") String email);

}
