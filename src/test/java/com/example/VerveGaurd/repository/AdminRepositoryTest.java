package com.example.VerveGaurd.repository;

import com.example.VerveGaurd.model.Admin;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
class AdminRepositoryTest {

    @Autowired
    private AdminRepository adminRepository;

    @Test
    void existsByEmail_shouldReturnTrue_whenExists() {
        // Given
        Admin admin = new Admin();
        admin.setId("admin-123");
        admin.setName("Test");
        admin.setEmail("test@example.com");
        admin.setPassword("pass");
        admin.setRole("Admin");

        adminRepository.save(admin);

        // When
        boolean exists = adminRepository.existsByEmail("test@example.com");

        // Then
        assertTrue(exists);
    }

    @Test
    void findByEmail_shouldReturnAdmin() {
        // Given
        Admin admin = new Admin();
        admin.setId("admin-123");
        admin.setName("Test");
        admin.setEmail("test@example.com");
        admin.setPassword("pass");
        admin.setRole("Admin");

        adminRepository.save(admin);

        // When
        Optional<Admin> found = adminRepository.findByEmail("test@example.com");

        // Then
        assertTrue(found.isPresent());
        assertEquals("Test", found.get().getName());
    }
}
