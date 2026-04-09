 package com.example.VerveGaurd.service;

import com.example.VerveGaurd.dto.Status;
import com.example.VerveGaurd.dto.TransactionRequestDTO;
import com.example.VerveGaurd.dto.TransactionResponseDTO;
import com.example.VerveGaurd.exception.ResourceNotFoundException;
import com.example.VerveGaurd.exception.RateLimitException;
import com.example.VerveGaurd.model.Merchant;
import com.example.VerveGaurd.repository.MerchantRepository;
import com.example.VerveGaurd.repository.TransactionRepository;
import jakarta.persistence.EntityManager;
import org.springframework.transaction.annotation.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

 @Service
 @RequiredArgsConstructor
 @Transactional
 @Slf4j
public class TransactionService {
     private final MerchantRepository merchantRepository;
     private final TransactionRepository transactionRepository;
     private final RateLimiterService rateLimiterService;
     private final EntityManager entityManager;
     private final MerchantService merchantService;
     private final TransactionLogService transactionLogService;



     @Transactional
     public TransactionResponseDTO processTransaction(TransactionRequestDTO dto, String ipAddress) {
         //validate that merchant actually exists in the table
         Merchant merchant = merchantRepository.findById(dto.getMerchantId()).orElseThrow(() -> new ResourceNotFoundException("Merchant with this ID does not exist"));

         // Force refresh from DB — bypasses Hibernate cache
         entityManager.refresh(merchant);

         // DEBUG - log what we read from DB
         log.info("Merchant ID: {}", merchant.getId());
         log.info("Is Blacklisted: {}", merchant.isBlacklisted());
         log.info("Blacklisted At: {}", merchant.getBlacklistedAt());

         TransactionResponseDTO response = new TransactionResponseDTO();



         // check 1: if merchant is already blacklisted

         if (merchant.isCurrentlyBlacklisted()) {
             log.info("Merchant IS blacklisted - checking expiry");
             transactionLogService.logFlaggedTransaction(dto, ipAddress);

             response.setReason("Blacklisted Merchant");
             response.setStatus(Status.FLAGGED);
             response.setFlagged(true);
             return response;

         }

         // Continue normal flow if not blacklisted or blacklist expired

         // check 2: check rate limiting by IP
         if (rateLimiterService.isRateLimited(ipAddress)) {
             log.info("Rate limited - blacklisting merchant");

             merchantService.blacklistMerchant(merchant);

             transactionLogService.logFlaggedTransaction(dto, ipAddress);

             // throw exception
             throw new RateLimitException(ipAddress);
         }

         //else return as cleared
         response.setStatus(Status.APPROVED);
         response.setReason("Merchant Cleared");
         response.setFlagged(false);
         return response;

     }


 }


