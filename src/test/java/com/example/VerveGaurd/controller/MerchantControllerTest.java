package com.example.VerveGaurd.controller;

import com.example.VerveGaurd.dto.MerchantRequestDTO;
import com.example.VerveGaurd.dto.MerchantResponseDTO;
import com.example.VerveGaurd.service.MerchantService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MerchantController.class)
class MerchantControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private MerchantService merchantService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void batchAdd_shouldReturnCreated_whenValidRequest() throws Exception {
        // Given
        MerchantRequestDTO request = new MerchantRequestDTO();
        request.setName("Test Merchant");
        request.setEmail("test@example.com");
        request.setCardNumber("123456789");

        List<MerchantRequestDTO> requests = List.of(request);

        MerchantResponseDTO response = new MerchantResponseDTO();
        response.setName("Test Merchant");
        response.setEmail("test@example.com");
        response.setIsBlacklisted(false);

        when(merchantService.batchAddMerchants(anyList())).thenReturn(List.of(response));

        // When & Then
        mockMvc.perform(post("/api/merchants/batch-save")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requests)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$[0].name").value("Test Merchant"))
                .andExpect(jsonPath("$[0].email").value("test@example.com"))
                .andExpect(jsonPath("$[0].isBlacklisted").value(false));
    }
}
