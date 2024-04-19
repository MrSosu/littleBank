package com.example.littleBank.services;

import com.example.littleBank.entities.Cliente;
import com.example.littleBank.repositories.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    public Cliente getClienteById(Long id) {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isEmpty()) throw new IllegalArgumentException("L'utente con id " + id + " non esiste!");
        return optionalCliente.get();
    }

    public List<Cliente> getAll() {
        return clienteRepository.findAll();
    }

    public Cliente createCliente(Cliente cliente) {
        clienteRepository.saveAndFlush(cliente);
        return cliente;
    }

    public Cliente updateCliente(Long id, Cliente newCliente) {
        Cliente cliente = Cliente.builder()
                .id(id)
                .nome(newCliente.getNome())
                .cognome(newCliente.getCognome())
                .codiceFiscale(newCliente.getCodiceFiscale())
                .email(newCliente.getEmail())
                .dataNascita(newCliente.getDataNascita())
                .indirizzo(newCliente.getIndirizzo())
                .comune(newCliente.getComune())
                .telefono(newCliente.getTelefono())
                .build();
        clienteRepository.saveAndFlush(cliente);
        return cliente;
    }

    public void deleteClienteById(Long id) {
        clienteRepository.deleteById(id);
    }

}
