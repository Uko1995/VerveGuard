package com.example.VerveGaurd.service;

import com.example.VerveGaurd.dto.TransactionRequestDTO;
import com.example.VerveGaurd.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class TransactionLogService {

    private final TransactionRepository transactionRepository;

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logFlaggedTransaction(TransactionRequestDTO dto, String ipAddress) {
        try {
            String traceId = MDC.get("traceId");
            transactionRepository.save(dto, true, "Rate limited", ipAddress, traceId);
            log.info("Flagged transaction logged for merchant {} with traceId {}", dto.getMerchantId(), traceId);
        } catch (Exception e) {
            log.error("Failed to log flagged transaction: {}", e.getMessage());
        }
    }
}