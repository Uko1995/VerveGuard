package com.example.VerveGaurd.repository;

import com.example.VerveGaurd.model.Merchant;
import jakarta.persistence.QueryHint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MerchantRepository extends JpaRepository <Merchant, Long> {
    @QueryHints(@QueryHint(name = "jakarta.persistence.cache.retrieveMode", value = "BYPASS"))
    Optional<Merchant> findBlacklistedMerchantById(long Id);

    @Modifying
    @Query("""
    UPDATE Merchant m
    SET m.blacklisted = false, m.blacklistedAt = null
    WHERE m.blacklisted = true
    AND m.blacklistedAt < :expiryTime
""")
    int clearExpired(@Param("expiryTime") LocalDateTime expiryTime);
}
