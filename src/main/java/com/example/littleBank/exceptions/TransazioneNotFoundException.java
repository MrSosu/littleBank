package com.example.littleBank.exceptions;

public class TransazioneNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "La transazione con tale id non esiste!";
    }
}
