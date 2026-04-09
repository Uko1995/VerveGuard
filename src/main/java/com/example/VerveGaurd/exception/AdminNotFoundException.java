package com.example.VerveGaurd.exception;

import org.springframework.http.HttpStatus;

public class AdminNotFoundException extends ApiException {

            public AdminNotFoundException(String message) {
                super(message, HttpStatus.NOT_FOUND);
            }
}
