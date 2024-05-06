package com.example.littleBank.security;

import com.example.littleBank.entities.Cliente;
import com.example.littleBank.request.RegistrationRequest;
import com.example.littleBank.response.AuthenticationResponse;
import lombok.var;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    public AuthenticationResponse register(RegistrationRequest request) {
        var user = Cliente.builder()
                .nome(request.getNome())
                .cognome(request.getCognome())
                .codiceFiscale(request.getCodiceFiscale())
                .comune(request.getComune())
                .indirizzo(request.getIndirizzo())
                .dataNascita(request.getDataNascita())
                .telefono(request.getTelefono())
                .email(request.getEmail())
                .password(request.getPassword());

    }

}
