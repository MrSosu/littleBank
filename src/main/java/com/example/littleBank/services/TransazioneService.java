package com.example.littleBank.services;

import com.example.littleBank.entities.Conto;
import com.example.littleBank.entities.Transazione;
import com.example.littleBank.exceptions.NonciainaliraException;
import com.example.littleBank.exceptions.TransazioneNotFoundException;
import com.example.littleBank.repositories.TransazioneRepository;
import com.example.littleBank.request.TransazioneRequest;
import com.example.littleBank.response.TransazioneResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TransazioneService {

    @Autowired
    private TransazioneRepository transazioneRepository;
    @Autowired
    private ContoService contoService;

    public TransazioneResponse getTransazioneResponseById(Long id) throws TransazioneNotFoundException {
        Optional<Transazione> optionalTransazione = transazioneRepository.findById(id);
        if (optionalTransazione.isEmpty()) throw new TransazioneNotFoundException();
        return convertFromEntity(optionalTransazione.get());
    }

    private Transazione getTransazioneById(Long id) throws TransazioneNotFoundException {
        Optional<Transazione> optionalTransazione = transazioneRepository.findById(id);
        if (optionalTransazione.isEmpty()) throw new TransazioneNotFoundException();
        return optionalTransazione.get();
    }

    public List<TransazioneResponse> getAllTransazioni() {
        return transazioneRepository.findAll().stream()
                .map(this::convertFromEntity)
                .toList();
    }

    @Transactional
    public TransazioneResponse createTransazione(TransazioneRequest transazioneRequest) throws NonciainaliraException {
        Transazione transazione = convertFromDTO(transazioneRequest);
        Conto mittente = transazione.getMittente();
        if (mittente.getCash() < transazioneRequest.getCash()) throw new NonciainaliraException();
        transazione.setTimestamp(LocalDateTime.now());
        contoService.updateContoAfterTransazione(transazione);
        transazioneRepository.saveAndFlush(transazione);
        return convertFromEntity(transazione);
    }

    public Transazione convertFromDTO(TransazioneRequest transazioneRequest) {
        return Transazione.builder()
                .mittente(contoService.getContoById(transazioneRequest.getIdContoMittente()))
                .destinatario(contoService.getContoById(transazioneRequest.getIdContoDestinatario()))
                .cash(transazioneRequest.getCash())
                .build();
    }

    public TransazioneResponse convertFromEntity(Transazione transazione) {
        return TransazioneResponse.builder()
                .idContoMittente(transazione.getMittente().getId())
                .idContoDestinatario(transazione.getDestinatario().getId())
                .cash(transazione.getCash())
                .timestamp(transazione.getTimestamp())
                .build();
    }

}
