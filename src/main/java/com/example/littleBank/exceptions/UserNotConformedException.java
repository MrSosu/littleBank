package com.example.littleBank.exceptions;

public class UserNotConformedException extends Exception{

    @Override
    public String getMessage() {
        return "Devi confermare il link via email per poter accedere!";
    }
}
