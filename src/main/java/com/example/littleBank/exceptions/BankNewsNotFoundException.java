package com.example.littleBank.exceptions;

public class BankNewsNotFoundException extends Exception {

    @Override
    public String getMessage() {
        return "News non presente!";
    }
}
