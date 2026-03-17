package com.example.VerveGaurd.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TransactionResponseDTO {
    private boolean isFlagged = false;
    private String reason;
    private Status status;
}
