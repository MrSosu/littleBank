package com.example.littleBank.controllers;

import com.example.littleBank.exceptions.NonciainaliraException;
import com.example.littleBank.exceptions.TransazioneNotFoundException;
import com.example.littleBank.request.TransazioneRequest;
import com.example.littleBank.response.TransazioneResponse;
import com.example.littleBank.services.TransazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/transazione")
public class TransazioneController {

    @Autowired
    private TransazioneService transazioneService;

    @GetMapping("/get/{id}")
    public TransazioneResponse getTransazioneById(@PathVariable Long id) throws TransazioneNotFoundException {
        return transazioneService.getTransazioneResponseById(id);
    }

    @GetMapping("/all")
    public List<TransazioneResponse> getAll() {
        return transazioneService.getAllTransazioni();
    }

    @PostMapping("/create")
    public TransazioneResponse createTransazione(@RequestBody TransazioneRequest transazioneRequest) throws NonciainaliraException {
        return transazioneService.createTransazione(transazioneRequest);
    }

}
