package com.example.littleBank.security;

import com.example.littleBank.entities.Cliente;
import com.example.littleBank.repositories.ClienteRepository;
import com.example.littleBank.request.AuthenticationRequest;
import com.example.littleBank.request.RegistrationRequest;
import com.example.littleBank.response.AuthenticationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    @Autowired
    private JwtService jwtService;
    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private PasswordEncoder passwordEncoder;

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
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var jwtToken = jwtService.generateToken(user);
        user.setRegistrationToken(jwtToken);
        clienteRepository.saveAndFlush(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
           authenticationRequest.getEmail(),
           authenticationRequest.getPassword()
        ));
        var user = clienteRepository.findClienteByEmail(authenticationRequest.getEmail());
        var jwtToken = jwtService.generateToken(user);
        return AuthenticationResponse.builder().token(jwtToken).build();
    }



}
