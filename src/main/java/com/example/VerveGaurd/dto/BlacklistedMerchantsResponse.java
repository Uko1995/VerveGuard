package com.example.VerveGaurd.dto;


import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class BlacklistedMerchantsResponse {
    private String name;
    private String email;
    private String reason;
    private Boolean isFlagged;
    private Status status;
    private LocalDateTime createdAt;
}
