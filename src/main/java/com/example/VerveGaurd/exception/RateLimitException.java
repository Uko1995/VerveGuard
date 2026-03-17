package com.example.VerveGaurd.exception;

import org.springframework.http.HttpStatus;

public class RateLimitException extends ApiException {

    public RateLimitException(String ipAddress) {
        super("Too many requests from IP: " + ipAddress, HttpStatus.TOO_MANY_REQUESTS);
    }
}