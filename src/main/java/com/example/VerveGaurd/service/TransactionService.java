 package com.example.VerveGaurd.service;

import com.example.VerveGaurd.dto.Status;
import com.example.VerveGaurd.dto.TransactionRequestDTO;
import com.example.VerveGaurd.dto.TransactionResponseDTO;
import com.example.VerveGaurd.exception.MerchantNotFoundException;
import com.example.VerveGaurd.exception.TransactionProcessingException;
import com.example.VerveGaurd.repository.MerchantRepository;
import com.example.VerveGaurd.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class TransactionService {
    private final MerchantRepository merchantRepository;
    private final TransactionRepository transactionRepository;
    private final RateLimiterService rateLimiterService;

    public TransactionService(MerchantRepository merchantRepository, TransactionRepository transactionRepository, RateLimiterService rateLimiterService) {
        this.merchantRepository = merchantRepository;
        this.transactionRepository = transactionRepository;
        this.rateLimiterService = rateLimiterService;
    }

    public TransactionResponseDTO processTransaction(TransactionRequestDTO dto) {
        //validate that merchant actually exists in the table
        merchantRepository.findById(dto.getMerchantId()).orElseThrow(() -> new MerchantNotFoundException(dto.getMerchantId()));

        boolean isFlagged = false;
        String reason = null;

        // check 1: if merchant is already blacklisted
        boolean blacklisted = merchantRepository.findBlacklistedMerchantById(dto.getMerchantId()).isPresent();
        if (blacklisted) {
            isFlagged = true;
            reason = "BLACKLISTED MERCHANT";
        }

        // check 2: check rate limiting by IP
        if (rateLimiterService.isRateLimited(dto.getIpAddress())) {
            isFlagged = true;
            reason = reason != null ? reason + ", RATE LIMITED" : "RATE LIMITED";
        }

        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setFlagged(isFlagged);
        response.setReason(reason);

        //log to repository if blacklisted and rate limited
        if(isFlagged) {
            try {
                transactionRepository.save(dto, true, reason);
            } catch (Exception e) {
                throw new TransactionProcessingException("Failed to log flagged merchant" + e.getMessage());
            }
            response.setStatus(Status.FLAGGED);
            return response;
        }

        //else return as cleared
        response.setStatus(Status.APPROVED);
        response.setReason("MERCHANT CLEARED");
        return response;

    }
}
