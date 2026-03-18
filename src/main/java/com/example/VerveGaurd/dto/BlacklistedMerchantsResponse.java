package com.example.VerveGaurd.dto;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlacklistedMerchantsResponse {
    private String name;
    private String email;
    private String reason;
    private Boolean isFlagged;
    private Status status;
}
