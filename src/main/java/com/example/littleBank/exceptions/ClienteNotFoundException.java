package com.example.littleBank.exceptions;


public class ClienteNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "Il cliente con tale id non esiste!";
    }
}
