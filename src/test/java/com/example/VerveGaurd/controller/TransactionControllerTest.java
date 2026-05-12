package com.example.VerveGaurd.controller;

import com.example.VerveGaurd.config.TestConfig;
import com.example.VerveGaurd.dto.Status;
import com.example.VerveGaurd.dto.TransactionRequestDTO;
import com.example.VerveGaurd.dto.TransactionResponseDTO;
import com.example.VerveGaurd.service.TransactionService;
import com.example.VerveGaurd.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(TransactionController.class)
@Import(TestConfig.class)
class TransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private TransactionService transactionService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void interceptTransaction_shouldReturnOk_whenProcessed() throws Exception {
        // Given
        TransactionRequestDTO request = new TransactionRequestDTO();
        request.setMerchantId(1L);
        request.setAmount(new BigDecimal("100.0"));
        request.setCardNumber("4532123456789010"); // Valid: 16-digit card number

        TransactionResponseDTO response = new TransactionResponseDTO();
        response.setStatus(Status.APPROVED);
        response.setReason("Merchant Cleared");
        response.setFlagged(false);

        when(transactionService.processTransaction(any(TransactionRequestDTO.class), eq("127.0.0.1"))).thenReturn(response);

        // When & Then
         mockMvc.perform(post("/api/transactions/intercept")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(objectMapper.writeValueAsString(request))
                         .header("X-Forwarded-For", "127.0.0.1")
                         .with(csrf()))
                 .andExpect(status().isOk())
                 .andExpect(jsonPath("$.success").value(true))
                 .andExpect(jsonPath("$.data.status").value("APPROVED"))
                 .andExpect(jsonPath("$.data.reason").value("Merchant Cleared"))
                 .andExpect(jsonPath("$.data.flagged").value(false));
    }
}
