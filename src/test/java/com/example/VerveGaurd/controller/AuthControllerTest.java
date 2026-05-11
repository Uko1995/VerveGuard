package com.example.VerveGaurd.controller;

import com.example.VerveGaurd.VerveGaurdApplication;
import com.example.VerveGaurd.dto.AdminLoginDTO;
import com.example.VerveGaurd.dto.AdminResponseDTO;
import com.example.VerveGaurd.dto.CreateAdminRequestDto;
import com.example.VerveGaurd.dto.LoginResponseDTO;
import com.example.VerveGaurd.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void login_shouldReturnOk_whenValid() throws Exception {
        // Given
        AdminLoginDTO request = new AdminLoginDTO();
        request.setEmail("test@example.com");
        request.setPassword("password");

        LoginResponseDTO response = new LoginResponseDTO();
        response.setToken("token");
        response.setTokenType("Bearer");
        response.setEmail("test@example.com");
        response.setRole("Admin");

        when(authService.login(any(AdminLoginDTO.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.token").value("token"))
                .andExpect(jsonPath("$.data.email").value("test@example.com"));
    }

    @Test
    @WithMockUser
    void createAdmin_shouldReturnCreated_whenValid() throws Exception {
        // Given
        CreateAdminRequestDto request = new CreateAdminRequestDto();
        request.setName("Test Admin");
        request.setEmail("test@example.com");
        request.setPassword("password");

        AdminResponseDTO response = new AdminResponseDTO();
        response.setName("Test Admin");
        response.setEmail("test@example.com");
        response.setRole("Admin");

        when(authService.createAdmin(any(CreateAdminRequestDto.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Test Admin"));
    }
}
