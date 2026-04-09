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

    private static final short BLACKLIST_DURATION_MINUTES = 5;

    @Column(name = "blacklisted", nullable = false)
    private boolean blacklisted = false;

    @Column(name = "blacklistedAt")
    private LocalDateTime blacklistedAt;

    @Column(name = "createdAt")
    private LocalDateTime createdAt;

    public boolean isCurrentlyBlacklisted() {
        return blacklistedAt != null &&
                LocalDateTime.now().isBefore(
                        blacklistedAt.plusMinutes(BLACKLIST_DURATION_MINUTES)
                );
    }

    public boolean isBlacklistExpired() {
        return blacklistedAt != null &&
                LocalDateTime.now().isAfter(
                        blacklistedAt.plusMinutes(BLACKLIST_DURATION_MINUTES)
                );
    }


}
