package com.example.littleBank.services;

import com.example.littleBank.entities.Conto;
import com.example.littleBank.entities.Transazione;
import com.example.littleBank.repositories.ContoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ContoService {

    @Autowired
    private ContoRepository contoRepository;

    public Conto getContoById(Long id) {
        Optional<Conto> optionalConto = contoRepository.findById(id);
        if (optionalConto.isEmpty()) throw new IllegalArgumentException("il conto con id " + id + " non esiste!");
        return optionalConto.get();
    }

    public List<Conto> getAll() { return contoRepository.findAll(); }

    public Conto createConto(Conto conto) {
        contoRepository.saveAndFlush(conto);
        return conto;
    }

    public Conto updateConto(Long id, Conto newConto) {
        Optional<Conto> optionalConto = contoRepository.findById(id);
        if (optionalConto.isEmpty()) throw new IllegalArgumentException("il conto con id " + id + " non esiste!");
        Conto conto = Conto.builder()
                .id(id)
                .dataConto(optionalConto.get().getDataConto())
                .costo(newConto.getCosto())
                .cash(newConto.getCash())
                .clientiConto(optionalConto.get().getClientiConto())
                .build();
        contoRepository.saveAndFlush(conto);
        return conto;
    }

    public void deleteContoById(Long id) {
        contoRepository.deleteById(id);
    }

    public void updateContoAfterTransazione(Transazione transazione) {
        Conto contoMittente = transazione.getMittente();
        Conto contoDestinatario = transazione.getDestinatario();
        contoMittente.setCash(contoMittente.getCash() - transazione.getCash());
        contoDestinatario.setCash(contoDestinatario.getCash() + transazione.getCash());
        contoRepository.saveAndFlush(contoMittente);
        contoRepository.saveAndFlush(contoDestinatario);
    }

}
