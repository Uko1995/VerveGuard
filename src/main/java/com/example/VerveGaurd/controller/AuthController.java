package com.example.VerveGaurd.controller;

import com.example.VerveGaurd.dto.AdminLoginDTO;
import com.example.VerveGaurd.dto.AdminResponseDTO;
import com.example.VerveGaurd.dto.CreateAdminRequestDto;
import com.example.VerveGaurd.dto.LoginResponseDTO;
import com.example.VerveGaurd.resposne.ApiResponse;
import com.example.VerveGaurd.security.JwtUtil;
import com.example.VerveGaurd.service.AuthService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {


    private final AuthService authService;

    public AuthController( AuthService authService) {

        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDTO>> login(@Valid @RequestBody AdminLoginDTO body) {
        LoginResponseDTO response = authService.login(body);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response, "Login successful"));
    }




    @PostMapping("/register")
    public ResponseEntity<ApiResponse<AdminResponseDTO>> createAdmin(@Valid @RequestBody CreateAdminRequestDto request) {
        AdminResponseDTO admin = authService.createAdmin(request);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(admin, "Admin has been created successfully"));
    }


}