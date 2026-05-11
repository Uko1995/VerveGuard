package com.example.VerveGaurd.dto;


import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDTO {
    private String token;
    private String tokenType;
    private String email;
    private String role;
    private LocalDateTime issuedAt;
    private LocalDateTime expiresAt;

}
