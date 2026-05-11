package com.example.VerveGaurd.service;

import com.example.VerveGaurd.dto.MerchantRequestDTO;
import com.example.VerveGaurd.dto.MerchantResponseDTO;
import com.example.VerveGaurd.exception.NullMerchantRequestException;
import com.example.VerveGaurd.model.Merchant;
import com.example.VerveGaurd.repository.MerchantRepository;
import com.example.VerveGaurd.service.MerchantService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MerchantServiceTest {

    @Mock
    private MerchantRepository merchantRepository;

    @InjectMocks
    private MerchantService merchantService;

    @Test
    void batchAddMerchants_shouldThrowException_whenNullFields() {
        // Given
        MerchantRequestDTO dto1 = new MerchantRequestDTO();
        dto1.setName("Name");
        dto1.setEmail(null); // null email
        dto1.setCardNumber("123");

        MerchantRequestDTO dto2 = new MerchantRequestDTO();
        dto2.setName("Name2");
        dto2.setEmail("email2");
        dto2.setCardNumber("456");

        List<MerchantRequestDTO> merchants = List.of(dto1, dto2);

        // When & Then
        assertThrows(NullMerchantRequestException.class, () -> merchantService.batchAddMerchants(merchants));
    }

    @Test
    void batchAddMerchants_shouldSaveAndReturnResponses() {
        // Given
        MerchantRequestDTO dto = new MerchantRequestDTO();
        dto.setName("Test Merchant");
        dto.setEmail("test@example.com");
        dto.setCardNumber("123456789");

        List<MerchantRequestDTO> merchants = List.of(dto);

        Merchant savedMerchant = new Merchant();
        savedMerchant.setId(1L);
        savedMerchant.setName("Test Merchant");
        savedMerchant.setEmail("test@example.com");
        savedMerchant.setCardNumber("123456789");
        savedMerchant.setBlacklisted(false);
        savedMerchant.setCreatedAt(LocalDateTime.now());

        when(merchantRepository.saveAll(anyList())).thenReturn(List.of(savedMerchant));

        // When
        List<MerchantResponseDTO> responses = merchantService.batchAddMerchants(merchants);

        // Then
        assertEquals(1, responses.size());
        MerchantResponseDTO response = responses.get(0);
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Test Merchant", response.getName());
        assertFalse(response.getIsBlacklisted());

        verify(merchantRepository, times(1)).saveAll(anyList());
    }

    @Test
    void blacklistMerchant_shouldUpdateMerchant() {
        // Given
        Merchant merchant = new Merchant();
        merchant.setId(1L);
        merchant.setBlacklisted(false);

        // When
        merchantService.blacklistMerchant(merchant);

        // Then
        assertTrue(merchant.isBlacklisted());
        assertNotNull(merchant.getBlacklistedAt());
        verify(merchantRepository, times(1)).saveAndFlush(merchant);
    }
}
