package com.example.VerveGaurd.dto;


import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
public class LoginResponseDTO {
    private String token;
    private String tokenType;
    private String email;
    private String role;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;

}
