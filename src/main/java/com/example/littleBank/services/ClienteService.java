package com.example.littleBank.services;

import com.example.littleBank.entities.Cliente;
import com.example.littleBank.entities.Conto;
import com.example.littleBank.repositories.ClienteRepository;
import com.example.littleBank.request.CreateContoRequest;
import com.example.littleBank.response.CreateContoResponse;
import com.example.littleBank.response.GetClienteResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;
    @Autowired
    private ContoService contoService;

    public GetClienteResponse getClienteById(Long id) {
        Optional<Cliente> optionalCliente = clienteRepository.findById(id);
        if (optionalCliente.isEmpty()) throw new IllegalArgumentException("L'utente con id " + id + " non esiste!");
        return convertDTO(optionalCliente.get());
    }

    public List<GetClienteResponse> getAll() {
        return clienteRepository.findAll().stream().map(this::convertDTO).toList();
    }

    public GetClienteResponse createCliente(Cliente cliente) {
        clienteRepository.saveAndFlush(cliente);
        return convertDTO(cliente);
    }

    public GetClienteResponse updateCliente(Long id, Cliente newCliente) {
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
        return convertDTO(cliente);
    }

    public void deleteClienteById(Long id) {
        clienteRepository.deleteById(id);
    }

    public CreateContoResponse createConto(CreateContoRequest createContoRequest) {
        if (createContoRequest.getClienti().isEmpty()) throw new IllegalArgumentException("deve esserci almeno un cliente intestatario del conto!");
        List<Cliente> clientiConto = new ArrayList<>();
        createContoRequest.getClienti().forEach(id -> clientiConto.add(convertFromDTO(getClienteById(id))));
        Conto conto = Conto.builder()
                .costo(createContoRequest.getCosto())
                .cash(createContoRequest.getCash())
                .clientiConto(clientiConto)
                .dataConto(LocalDate.now())
                .build();
        contoService.createConto(conto);
        createContoRequest.getClienti().forEach(id -> {
            Cliente cliente = convertFromDTO(getClienteById(id));
            cliente.getContiUtente().add(conto);
        });
        CreateContoResponse createContoResponse = CreateContoResponse.builder()
                .id_conto(conto.getId())
                .dataConto(conto.getDataConto())
                .cash(conto.getCash())
                .costo(conto.getCosto())
                .id_clienti(createContoRequest.getClienti())
                .build();
        return createContoResponse;
    }

    private GetClienteResponse convertDTO(Cliente cliente) {
        List<Long> id_conti = new ArrayList<>();
        cliente.getContiUtente().forEach(conto -> {
            id_conti.add(conto.getId());
        });
        GetClienteResponse getClienteResponse = GetClienteResponse.builder()
                .id(cliente.getId())
                .indirizzo(cliente.getIndirizzo())
                .codiceFiscale(cliente.getCodiceFiscale())
                .nome(cliente.getNome())
                .cognome(cliente.getCognome())
                .email(cliente.getEmail())
                .telefono(cliente.getTelefono())
                .dataNascita(cliente.getDataNascita())
                .comune(cliente.getComune())
                .id_conti_utente(id_conti)
                .build();
        return getClienteResponse;
    }

    private Cliente convertFromDTO(GetClienteResponse getClienteResponse) {
        List<Conto> conti_utente = new ArrayList<>();
        getClienteResponse.getId_conti_utente().forEach(id -> conti_utente.add(contoService.getContoById(id)));
        Cliente cliente = Cliente.builder()
                .email(getClienteResponse.getEmail())
                .nome(getClienteResponse.getNome())
                .cognome(getClienteResponse.getCognome())
                .comune(getClienteResponse.getComune())
                .indirizzo(getClienteResponse.getIndirizzo())
                .dataNascita(getClienteResponse.getDataNascita())
                .codiceFiscale(getClienteResponse.getCodiceFiscale())
                .telefono(getClienteResponse.getTelefono())
                .id(getClienteResponse.getId())
                .contiUtente(conti_utente)
                .build();
        return cliente;
    }


}
