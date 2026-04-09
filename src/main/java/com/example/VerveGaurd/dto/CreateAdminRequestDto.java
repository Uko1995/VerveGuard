package com.example.VerveGaurd.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateAdminRequestDto {

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Email field is required")
    @Email(message = "Email format is not correct")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, max = 20, message = "Password should be between 8 and 20 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).{8,20}$", message = "Password must contain at least 1 uppercase, lowercase and special characters")
    private String password;

}
