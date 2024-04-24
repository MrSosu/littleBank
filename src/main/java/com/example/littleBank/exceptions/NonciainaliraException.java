package com.example.littleBank.exceptions;

public class NonciainaliraException extends Exception {

    @Override
    public String getMessage() {
        return "a purciaro non ciai na lira!";
    }
}
