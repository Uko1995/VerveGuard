package com.example.VerveGaurd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "Merchant")
public class Merchant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Id;

    @Column(nullable = false)
    private String cardNumber;



    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String email;

    private static final short blacklistDurationMinutes = 5;

    private boolean isBlacklisted = false;

    private LocalDateTime blacklistedAt;
    private LocalDateTime createdAt;

    public boolean isBlacklistExpired() {
        if (!isBlacklisted || blacklistedAt == null) {
            return false;
        }
        LocalDateTime expiryTime = blacklistedAt.plusMinutes(blacklistDurationMinutes);
        return LocalDateTime.now().isAfter(expiryTime);
    }


}
