package com.example.littleBank.security;

import com.example.littleBank.exceptions.UserNotConformedException;
import com.example.littleBank.request.AuthenticationRequest;
import com.example.littleBank.request.RegistrationRequest;
import com.example.littleBank.response.AuthenticationResponse;
import com.example.littleBank.response.ErrorResponse;
import com.example.littleBank.response.GenericResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@RequestBody RegistrationRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) {
        try {
            return ResponseEntity.ok(authenticationService.authenticate(authenticationRequest));
        } catch (UserNotConformedException e) {
            return new ResponseEntity<>(new ErrorResponse("UserNotConfirmedException", e.getMessage()), HttpStatus.FORBIDDEN);
        }

    }



    @GetMapping("/confirm")
    public ResponseEntity<?> confirmRegistration(@RequestParam Long id, @RequestParam String token) {
        if (authenticationService.confirmRegistration(id, token)) {
            return new ResponseEntity<>(new GenericResponse("conferma avvenuta con successo!"), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ErrorResponse("NotConfirmedException", "OPS! Qualcosa è andato storto con la conferma del tuo account!"), HttpStatus.BAD_REQUEST);
    }

}
