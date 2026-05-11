package com.example.VerveGaurd.service;

import com.example.VerveGaurd.dto.Status;
import com.example.VerveGaurd.dto.TransactionRequestDTO;
import com.example.VerveGaurd.dto.TransactionResponseDTO;
import com.example.VerveGaurd.exception.RateLimitException;
import com.example.VerveGaurd.exception.ResourceNotFoundException;
import com.example.VerveGaurd.model.Merchant;
import com.example.VerveGaurd.repository.MerchantRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {

    @Mock
    private MerchantRepository merchantRepository;

    @Mock
    private RateLimiterService rateLimiterService;

    @Mock
    private MerchantService merchantService;

    @Mock
    private TransactionLogService transactionLogService;

    @InjectMocks
    private TransactionService transactionService;

    @Test
    void processTransaction_shouldThrowException_whenMerchantNotFound() {
        // Given
        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setMerchantId(1L);

        when(merchantRepository.findByIdFresh(1L)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> transactionService.processTransaction(dto, "127.0.0.1"));
    }

    @Test
    void processTransaction_shouldFlag_whenMerchantBlacklisted() {
        // Given
        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setMerchantId(1L);

        Merchant merchant = new Merchant();
        merchant.setId(1L);
        merchant.setBlacklisted(true);
        merchant.setBlacklistedAt(LocalDateTime.now().minusMinutes(1)); // within 5 min

        when(merchantRepository.findByIdFresh(1L)).thenReturn(Optional.of(merchant));

        // When
        TransactionResponseDTO response = transactionService.processTransaction(dto, "127.0.0.1");

        // Then
        assertEquals(Status.FLAGGED, response.getStatus());
        assertEquals("Blacklisted Merchant", response.getReason());
        assertTrue(response.isFlagged());

        verify(transactionLogService, times(1)).logFlaggedTransaction(dto, "127.0.0.1");
    }

    @Test
    void processTransaction_shouldThrowRateLimitException_whenRateLimited() {
        // Given
        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setMerchantId(1L);

        Merchant merchant = new Merchant();
        merchant.setId(1L);
        merchant.setBlacklisted(false);

        when(merchantRepository.findByIdFresh(1L)).thenReturn(Optional.of(merchant));
        when(rateLimiterService.isRateLimited("127.0.0.1")).thenReturn(true);

        // When & Then
        assertThrows(RateLimitException.class, () -> transactionService.processTransaction(dto, "127.0.0.1"));

        verify(merchantService, times(1)).blacklistMerchant(merchant);
        verify(transactionLogService, times(1)).logFlaggedTransaction(dto, "127.0.0.1");
    }

    @Test
    void processTransaction_shouldApprove_whenNotBlacklistedAndNotRateLimited() {
        // Given
        TransactionRequestDTO dto = new TransactionRequestDTO();
        dto.setMerchantId(1L);

        Merchant merchant = new Merchant();
        merchant.setId(1L);
        merchant.setBlacklisted(false);

        when(merchantRepository.findByIdFresh(1L)).thenReturn(Optional.of(merchant));
        when(rateLimiterService.isRateLimited("127.0.0.1")).thenReturn(false);

        // When
        TransactionResponseDTO response = transactionService.processTransaction(dto, "127.0.0.1");

        // Then
        assertEquals(Status.APPROVED, response.getStatus());
        assertEquals("Merchant Cleared", response.getReason());
        assertFalse(response.isFlagged());
    }
}
