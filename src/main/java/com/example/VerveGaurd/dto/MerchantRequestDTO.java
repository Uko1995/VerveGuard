package com.example.VerveGaurd.dto;

import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantRequestDTO {
    private String cardNumber;

    private String name;

    private String email;
}
