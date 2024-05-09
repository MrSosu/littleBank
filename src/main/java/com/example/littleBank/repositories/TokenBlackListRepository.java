package com.example.littleBank.repositories;

import com.example.littleBank.entities.TokenBlackList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TokenBlackListRepository extends JpaRepository<TokenBlackList, Long> {

    @Query(value = "SELECT * FROM token_black_list WHERE cliente_id = :id_cliente", nativeQuery = true)
    List<TokenBlackList> getTokenBlackListFromClienteId(@Param("id_cliente") Long id_cliente);

}
