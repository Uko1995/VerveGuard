package com.example.VerveGaurd.repository;

import com.example.VerveGaurd.model.Merchant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MerchantRepository extends JpaRepository <Merchant, Long> {
    Optional<Merchant> findBlacklistedMerchantById(long Id);
}
