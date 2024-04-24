package com.example.littleBank.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleIllegalArgumentException(IllegalArgumentException e) {
        ErrorResponse errorResponse = new ErrorResponse("Illegal Argument Exception", "Id non presente nella tabella");
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ClienteNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleClienteNotFoundException(ClienteNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse("ClienteNotFoundException", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(TransazioneNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleTransazioneNotFoundException(TransazioneNotFoundException e) {
        ErrorResponse errorResponse = new ErrorResponse("TransazioneNotFoundException", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(NonciainaliraException.class)
    public ResponseEntity<ErrorResponse> handleNonciainaliraException(NonciainaliraException e) {
        ErrorResponse errorResponse = new ErrorResponse("NonciainaliraException", e.getMessage());
        return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
    }

}
