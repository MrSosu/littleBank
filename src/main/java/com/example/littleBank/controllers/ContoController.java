package com.example.littleBank.controllers;

import com.example.littleBank.entities.Conto;
import com.example.littleBank.services.ContoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/conto")
public class ContoController {

    @Autowired
    private ContoService contoService;

    @GetMapping("/get/{id}")
    public ResponseEntity<?> getContoById(@PathVariable Long id) {
            Conto myConto = contoService.getContoById(id);
            return new ResponseEntity<>(myConto, HttpStatus.OK);

    }

    @GetMapping("/all")
    public ResponseEntity<List<Conto>> getAllConti() {
        return new ResponseEntity<>(contoService.getAll(), HttpStatus.OK);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateConto(@PathVariable Long id, @RequestBody Conto conto) {
        try {
            return new ResponseEntity<>(contoService.updateConto(id, conto), HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteContoById(@PathVariable Long id) {
        try {
            return new ResponseEntity<>("Conto con id " + id + " eliminato con successo", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}
