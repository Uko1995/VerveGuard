package com.example.VerveGaurd.service;


import com.example.VerveGaurd.repository.MerchantRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Slf4j
@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final MerchantRepository merchantRepository;
    private static final short BLACKLISTED_DURATION_MINUTES = 5;

    @Scheduled(fixedRate = 120000)
    @Transactional
    public void clearBlacklistedMerchants() {
        LocalDateTime expiryTime =  LocalDateTime.now().minusMinutes(BLACKLISTED_DURATION_MINUTES);


        int updatedMerchants = merchantRepository.clearExpired(expiryTime);
        log.info("Cleared {} expired blacklisted merchants", updatedMerchants);
    }
}

