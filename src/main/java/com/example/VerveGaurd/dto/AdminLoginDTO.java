package com.example.VerveGaurd.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminLoginDTO {
    @NotBlank(message = "Email is required")
    @Email(message = "Email must be in the correct format")
    private String email;

    @NotBlank(message = "Password must be provided")
    private String password;
}
