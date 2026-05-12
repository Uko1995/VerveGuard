package com.example.VerveGaurd.config;

import com.example.VerveGaurd.security.JwtUtil;
import com.example.VerveGaurd.service.AuthService;
import com.example.VerveGaurd.service.MerchantService;
import com.example.VerveGaurd.service.TransactionService;
import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

/**
 * Test configuration that provides mock beans for testing.
 * This replaces the deprecated @MockBean annotation from Spring Boot 3.4+
 */
@TestConfiguration
public class TestConfig {

    @Bean
    @Primary
    public AuthService authService() {
        return Mockito.mock(AuthService.class);
    }

    @Bean
    @Primary
    public MerchantService merchantService() {
        return Mockito.mock(MerchantService.class);
    }

    @Bean
    @Primary
    public TransactionService transactionService() {
        return Mockito.mock(TransactionService.class);
    }

    @Bean
    @Primary
    public JwtUtil jwtUtil() {
        return Mockito.mock(JwtUtil.class);
    }
}

