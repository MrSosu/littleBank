package com.example.littleBank.controllers;

import com.example.littleBank.entities.Cliente;
import com.example.littleBank.services.ClienteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cliente")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/get/{id}")
    public Cliente getClienteById(@PathVariable Long id) {
        return clienteService.getClienteById(id);
    }

    @GetMapping("/all")
    public List<Cliente> getAllClienti() {
        return clienteService.getAll();
    }

    @PostMapping("/create")
    public Cliente createCliente(@RequestBody Cliente cliente) {
        return clienteService.createCliente(cliente);
    }

    @PutMapping("/update/{id}")
    public Cliente updateCliente(@PathVariable Long id, @RequestBody Cliente cliente) {
        return clienteService.updateCliente(id, cliente);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteCliente(@PathVariable Long id) {
        clienteService.deleteClienteById(id);
    }

}
