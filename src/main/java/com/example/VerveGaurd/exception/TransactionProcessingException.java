package com.example.VerveGaurd.exception;

import org.springframework.http.HttpStatus;

public class TransactionProcessingException extends ApiException {

    public TransactionProcessingException(String message) {
        super(message, HttpStatus.INTERNAL_SERVER_ERROR);
    }
}