package com.example.VerveGaurd.exception;

import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends ApiException {

    public ResourceNotFoundException(String message) {
            super(message, HttpStatus.NOT_FOUND);
    }
}