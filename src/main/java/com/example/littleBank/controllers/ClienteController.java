package com.example.littleBank.controllers;

import com.example.littleBank.entities.Cliente;
import com.example.littleBank.entities.Conto;
import com.example.littleBank.request.CreateContoRequest;
import com.example.littleBank.response.GetClienteResponse;
import com.example.littleBank.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/get/{id}")
    public GetClienteResponse getClienteById(@PathVariable Long id) {
        return clienteService.getClienteById(id);
    }

    @GetMapping("/all")
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


}
