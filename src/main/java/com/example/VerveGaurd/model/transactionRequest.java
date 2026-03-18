package com.example.VerveGaurd.model;

import com.example.VerveGaurd.dto.Status;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Setter
@Getter
@Entity
@Table(name = "TransactionRequests")
public class transactionRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long Id;

    private String cardNumber;
    private BigDecimal amount;
    private String ipAddress;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchantId", nullable = false)
    private Merchant merchant;

    private boolean isFlagged;
    private String reason;
    private LocalDateTime createdAt;
}
