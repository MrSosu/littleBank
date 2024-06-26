package com.example.littleBank.security;

import com.example.littleBank.entities.Cliente;
import com.example.littleBank.entities.TokenBlackList;
import com.example.littleBank.enums.Role;
import com.example.littleBank.exceptions.UserNotConformedException;
import com.example.littleBank.repositories.ClienteRepository;
import com.example.littleBank.request.AuthenticationRequest;
import com.example.littleBank.request.RegistrationRequest;
import com.example.littleBank.response.AuthenticationResponse;
import com.example.littleBank.services.TokenBlackListService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

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
    @Autowired
    private TokenBlackListService tokenBlackListService;
    @Autowired
    private JavaMailSender javaMailSender;

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
                .role(Role.USER)
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        var jwtToken = jwtService.generateToken(user);
        user.setRegistrationToken(jwtToken);
        clienteRepository.saveAndFlush(user);
       // javaMailSender.send(createConfirmationMail(user));
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public AuthenticationResponse authenticate(AuthenticationRequest authenticationRequest) throws UserNotConformedException {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
           authenticationRequest.getEmail(),
           authenticationRequest.getPassword()
        ));
        var user = clienteRepository.findClienteByEmail(authenticationRequest.getEmail());
        if (user.getRole().equals(Role.TOCONFIRM)) {
            throw new UserNotConformedException();
        }
        var jwtToken = jwtService.generateToken(user);
        if (tokenBlackListService.tokenNotValidFromClienteById(user.getId()).contains(jwtToken)) {
            String email = jwtService.extractUsername(jwtToken);
            // Carica l'utente dal database
            UserDetails userDetails = clienteRepository.findClienteByEmail(email);

            // Genera un nuovo token con le informazioni aggiornate
            String newToken = jwtService.generateToken(userDetails);
            return AuthenticationResponse.builder().token(newToken).build();
        }
        return AuthenticationResponse.builder().token(jwtToken).build();
    }

    public void logout(HttpServletRequest httpRequest, Long id) {
        String token = extractTokenFromRequest(httpRequest);
        TokenBlackList tokenBlackList = TokenBlackList.builder()
                        .cliente(clienteRepository.getReferenceById(id))
                                .token(token)
                                        .build();
        tokenBlackListService.createTokenBlackList(tokenBlackList);
    }

    public String extractTokenFromRequest(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("Authorization");
        if (StringUtils.hasText(authorizationHeader) && authorizationHeader.startsWith("Bearer ")) {
            return authorizationHeader.substring(7);
        }
        return null;
    }

    public boolean confirmRegistration(Long id, String token) {
        Cliente cliente = clienteRepository.getReferenceById(id);
        if (cliente.getRegistrationToken().equals(token)) {
            cliente.setRole(Role.USER);
            clienteRepository.saveAndFlush(cliente);
            return true;
        }
        return false;
    }

    public SimpleMailMessage createConfirmationMail(Cliente cliente) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(cliente.getEmail());
        message.setSubject("CONFERMA REGISTRAZIONE");
        String url = "http://localhost:8080/auth/confirm?id=" + cliente.getId() + "&token=" + cliente.getRegistrationToken();
        message.setText("Clicca qui per confermare la registrazione: " + url);
        return message;
    }

}
