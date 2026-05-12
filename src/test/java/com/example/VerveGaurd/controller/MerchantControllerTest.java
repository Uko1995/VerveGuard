package com.example.VerveGaurd.controller;

import com.example.VerveGaurd.config.TestConfig;
import com.example.VerveGaurd.dto.MerchantRequestDTO;
import com.example.VerveGaurd.dto.MerchantResponseDTO;
import com.example.VerveGaurd.service.MerchantService;
import com.example.VerveGaurd.security.JwtUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MerchantController.class)
@Import(TestConfig.class)
class MerchantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private MerchantService merchantService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @WithMockUser(roles = "ADMIN")
    void batchAdd_shouldReturnCreated_whenValidRequest() throws Exception {
        // Given
        MerchantRequestDTO request = new MerchantRequestDTO();
        request.setName("Test Merchant");
        request.setEmail("test@example.com");
        request.setCardNumber("4532123456789010"); // Valid: 16-digit card number

        List<MerchantRequestDTO> requests = List.of(request);

        MerchantResponseDTO response = new MerchantResponseDTO();
        response.setName("Test Merchant");
        response.setEmail("test@example.com");
        response.setIsBlacklisted(false);

        when(merchantService.batchAddMerchants(anyList())).thenReturn(List.of(response));

        // When & Then
         mockMvc.perform(post("/api/merchants/batch-save")
                         .contentType(MediaType.APPLICATION_JSON)
                         .content(objectMapper.writeValueAsString(requests))
                         .with(csrf()))
                 .andExpect(status().isCreated())
                 .andExpect(jsonPath("$[0].name").value("Test Merchant"))
                 .andExpect(jsonPath("$[0].email").value("test@example.com"))
                 .andExpect(jsonPath("$[0].isBlacklisted").value(false));
    }
}
