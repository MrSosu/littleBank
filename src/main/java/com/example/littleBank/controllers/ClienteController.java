package com.example.littleBank.controllers;

import com.example.littleBank.entities.Cliente;
import com.example.littleBank.entities.Conto;
import com.example.littleBank.exceptions.ClienteNotFoundException;
import com.example.littleBank.request.CreateContoRequest;
import com.example.littleBank.response.GetClienteResponse;
import com.example.littleBank.security.AuthenticationService;
import com.example.littleBank.services.ClienteService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
    @Autowired
    private AuthenticationService authenticationService;

    @GetMapping("/get/{id}")
    public GetClienteResponse getClienteById(@PathVariable Long id) throws ClienteNotFoundException {
        return clienteService.getClienteById(id);
    }

    @GetMapping("/all")
    @Secured("ADMIN")
    public List<GetClienteResponse> getAllClienti() {
        return clienteService.getAll();
    }

    @PostMapping("/create")
    public GetClienteResponse createCliente(@RequestBody Cliente cliente) {
        return clienteService.createCliente(cliente);
    }

    @PutMapping("/update/{id}")
    public GetClienteResponse updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteService.updateCliente(id, cliente);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCliente(@PathVariable Long id) {
        clienteService.deleteClienteById(id);
    }

    @PostMapping("/open_conto")
    public ResponseEntity<?> openConto(@RequestBody CreateContoRequest createContoRequest) {
        try {
            return new ResponseEntity<>(clienteService.createConto(createContoRequest), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/update/role")
    @Secured("SUPERADMIN")
    public ResponseEntity<String> updateRole(@RequestParam Long id, @RequestParam String new_role) throws ClienteNotFoundException {
        clienteService.updateRole(id, new_role);
        return new ResponseEntity<>("Ruolo aggiornato con successo", HttpStatus.CREATED);
    }

    @PostMapping("/logout/{id}")
    public void logout(HttpServletRequest httpRequest, @PathVariable Long id) {
        authenticationService.logout(httpRequest, id);
    }

}
