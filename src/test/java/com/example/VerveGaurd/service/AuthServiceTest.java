package com.example.VerveGaurd.service;

import com.example.VerveGaurd.dto.AdminLoginDTO;
import com.example.VerveGaurd.dto.AdminResponseDTO;
import com.example.VerveGaurd.dto.CreateAdminRequestDto;
import com.example.VerveGaurd.dto.LoginResponseDTO;
import com.example.VerveGaurd.exception.BadRequestException;
import com.example.VerveGaurd.exception.DuplicateResourceException;
import com.example.VerveGaurd.exception.ResourceNotFoundException;
import com.example.VerveGaurd.model.Admin;
import com.example.VerveGaurd.repository.AdminRepository;
import com.example.VerveGaurd.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthService authService;

    @Test
    void createAdmin_shouldThrowException_whenEmailExists() {
        // Given
        CreateAdminRequestDto request = new CreateAdminRequestDto();
        request.setEmail("existing@example.com");

        when(adminRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // When & Then
        assertThrows(DuplicateResourceException.class, () -> authService.createAdmin(request));
    }

    @Test
    void createAdmin_shouldCreateAndReturnResponse() {
        // Given
        CreateAdminRequestDto request = new CreateAdminRequestDto();
        request.setName("Test Admin");
        request.setEmail("test@example.com");
        request.setPassword("password");

        Admin savedAdmin = new Admin();
        savedAdmin.setId("admin-123");
        savedAdmin.setName("Test Admin");
        savedAdmin.setEmail("test@example.com");
        savedAdmin.setRole("Admin");

        when(adminRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(adminRepository.save(any(Admin.class))).thenReturn(savedAdmin);

        // When
        AdminResponseDTO response = authService.createAdmin(request);

        // Then
        assertEquals("Test Admin", response.getName());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Admin", response.getRole());

        verify(adminRepository, times(1)).save(any(Admin.class));
    }

    @Test
    void login_shouldThrowException_whenAdminNotFound() {
        // Given
        AdminLoginDTO body = new AdminLoginDTO();
        body.setEmail("notfound@example.com");

        when(adminRepository.findByEmail("notfound@example.com")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(ResourceNotFoundException.class, () -> authService.login(body));
    }

    @Test
    void login_shouldThrowException_whenInvalidPassword() {
        // Given
        AdminLoginDTO body = new AdminLoginDTO();
        body.setEmail("test@example.com");
        body.setPassword("wrong");

        Admin admin = new Admin();
        admin.setPassword("hashed");

        when(adminRepository.findByEmail("test@example.com")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("wrong", "hashed")).thenReturn(false);

        // When & Then
        assertThrows(BadRequestException.class, () -> authService.login(body));
    }

    @Test
    void login_shouldReturnLoginResponse_whenValid() {
        // Given
        AdminLoginDTO body = new AdminLoginDTO();
        body.setEmail("test@example.com");
        body.setPassword("password");

        Admin admin = new Admin();
        admin.setEmail("test@example.com");
        admin.setRole("Admin");
        admin.setPassword("hashed");

        when(adminRepository.findByEmail("test@example.com")).thenReturn(Optional.of(admin));
        when(passwordEncoder.matches("password", "hashed")).thenReturn(true);
        when(jwtUtil.generateToken("test@example.com")).thenReturn("token");
        when(jwtUtil.getExpiration()).thenReturn(3600000L);

        // When
        LoginResponseDTO response = authService.login(body);

        // Then
        assertEquals("token", response.getToken());
        assertEquals("Bearer", response.getTokenType());
        assertEquals("test@example.com", response.getEmail());
        assertEquals("Admin", response.getRole());
        assertNotNull(response.getIssuedAt());
        assertNotNull(response.getExpiresAt());
    }
}
