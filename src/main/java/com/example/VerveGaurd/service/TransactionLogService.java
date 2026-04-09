package com.example.VerveGaurd.service;

import com.example.VerveGaurd.dto.TransactionRequestDTO;
import com.example.VerveGaurd.exception.TransactionProcessingException;
import com.example.VerveGaurd.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionLogService {

    private final TransactionRepository transactionRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFlaggedTransaction(TransactionRequestDTO dto, String ipAddress) {
        try {
            transactionRepository.save(dto, true, "Rate limited", ipAddress);
            log.info("Flagged transaction logged for merchant {}", dto.getMerchantId());
        } catch (Exception e) {
            log.error("Failed to log flagged transaction: {}", e.getMessage());
            throw new TransactionProcessingException("Failed to log transaction: " + e.getMessage());
        }
    }
}