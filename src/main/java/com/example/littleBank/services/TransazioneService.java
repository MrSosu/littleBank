package com.example.littleBank.services;

import com.example.littleBank.entities.Transazione;
import com.example.littleBank.exceptions.TransazioneNotFoundException;
import com.example.littleBank.repositories.TransazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TransazioneService {

    @Autowired
    private TransazioneRepository transazioneRepository;

    public Transazione getTransazioneById(Long id) throws TransazioneNotFoundException {
        Optional<Transazione> optionalTransazione = transazioneRepository.findById(id);
        if (optionalTransazione.isEmpty()) throw new TransazioneNotFoundException();
        return optionalTransazione.get();
    }

}
