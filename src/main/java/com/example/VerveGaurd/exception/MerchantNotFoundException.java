package com.example.VerveGaurd.exception;

import org.springframework.http.HttpStatus;

public class MerchantNotFoundException extends ApiException {

    public MerchantNotFoundException(Long merchantId) {
        super("Merchant with ID " + merchantId + " not found", HttpStatus.NOT_FOUND);
    }
}