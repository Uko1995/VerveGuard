package com.example.VerveGaurd.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class TransactionRequestDTO {
    private String cardNumber;
    private BigDecimal amount;
    private Long merchantId;
    private String ipAddress;
}
