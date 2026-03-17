package com.example.VerveGaurd.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class MerchantResponseDTO {
    private String name;
    private  String email;
    private Boolean isBlacklisted;
}
