package com.example.VerveGaurd.exception;

import com.example.VerveGaurd.dto.MerchantRequestDTO;
import org.springframework.http.HttpStatus;

public class NullMerchantRequestException extends ApiException {
    public NullMerchantRequestException() {
        super("Some merchants have some null fields", HttpStatus.BAD_REQUEST);
    }

}
