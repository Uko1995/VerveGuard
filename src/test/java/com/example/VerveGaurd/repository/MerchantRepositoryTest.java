package com.example.VerveGaurd.repository;

import com.example.VerveGaurd.model.Merchant;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class MerchantRepositoryTest {

    @Autowired
    private MerchantRepository merchantRepository;

    @Test
    void findByIdFresh_shouldReturnMerchant() {
        // Given
        Merchant merchant = new Merchant();
        merchant.setName("Test");
        merchant.setEmail("test@example.com");
        merchant.setCardNumber("123");
        merchant.setCreatedAt(LocalDateTime.now());

        Merchant saved = merchantRepository.save(merchant);

        // When
        Optional<Merchant> found = merchantRepository.findByIdFresh(saved.getId());

        // Then
        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getName());
    }

    @Test
    void clearExpired_shouldUpdateBlacklistedMerchants() {
        // Given
        Merchant merchant = new Merchant();
        merchant.setName("Test");
        merchant.setEmail("test@example.com");
        merchant.setCardNumber("123");
        merchant.setBlacklisted(true);
        merchant.setBlacklistedAt(LocalDateTime.now().minusMinutes(6)); // expired
        merchant.setCreatedAt(LocalDateTime.now());

        merchantRepository.save(merchant);

        // When
        int updated = merchantRepository.clearExpired(LocalDateTime.now().minusMinutes(5));

        // Then
        assertEquals(1, updated);

        Merchant updatedMerchant = merchantRepository.findById(merchant.getId()).orElseThrow();
        assertFalse(updatedMerchant.isBlacklisted());
        assertNull(updatedMerchant.getBlacklistedAt());
    }
}
