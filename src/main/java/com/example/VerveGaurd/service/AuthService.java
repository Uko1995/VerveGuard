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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class AuthService {

    private final AdminRepository adminRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthService(JwtUtil jwtUtil, PasswordEncoder passwordEncoder, AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    private Boolean EmailExists(String email) {
        return adminRepository.existsByEmail(email);
    }

    public AdminResponseDTO createAdmin(CreateAdminRequestDto request) {
        if (this.EmailExists(request.getEmail())) {
            throw new DuplicateResourceException("User with this email already exists");
        }

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String hashedPassword = encoder.encode(request.getPassword());

        Admin newAdmin = new Admin();
        newAdmin.setName(request.getName());
        newAdmin.setEmail(request.getEmail());
        newAdmin.setPassword(hashedPassword);
        newAdmin.setRole("Admin");
        newAdmin.setCreatedAt(LocalDateTime.now());

        Admin savedAdmin = adminRepository.save(newAdmin);

        return mapToResponse(savedAdmin);

    }


    public LoginResponseDTO login(AdminLoginDTO body) {



        Admin admin = adminRepository.findByEmail(body.getEmail()).orElseThrow(() ->  new ResourceNotFoundException("Admin with this email does not exist"));

        //check for wrong password
         if (!passwordEncoder.matches(body.getPassword(), admin.getPassword())) {
             throw new BadRequestException("Invalid credentials");
         }

         String token = jwtUtil.generateToken(body.getEmail());
         long expiration = jwtUtil.getExpiration();

        return LoginResponseDTO.builder()
                .token(token)
                .tokenType("Bearer")
                .email(admin.getEmail())
                .role(admin.getRole())
                .issuedAt(LocalDateTime.now())
                .expiresAt(LocalDateTime.now().plusSeconds(expiration / 1000))
                .build();


    }

    private AdminResponseDTO mapToResponse(Admin admin) {
        AdminResponseDTO response = new AdminResponseDTO();
        response.setName(admin.getName());
        response.setEmail(admin.getEmail());
        response.setRole(admin.getRole());

        return response;
    }
}
