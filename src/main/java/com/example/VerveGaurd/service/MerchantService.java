package com.example.VerveGaurd.service;

import com.example.VerveGaurd.dto.MerchantRequestDTO;
import com.example.VerveGaurd.dto.MerchantResponseDTO;
import com.example.VerveGaurd.exception.NullMerchantRequestException;
import com.example.VerveGaurd.model.Merchant;
import com.example.VerveGaurd.repository.MerchantRepository;
import com.example.VerveGaurd.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class MerchantService {
    private final MerchantRepository  merchantRepository;


    public List<MerchantResponseDTO> batchAddMerchants(List<MerchantRequestDTO> merchants){
        boolean hasNull = merchants.stream().anyMatch(merchant -> merchant.getEmail() == null || merchant.getName() == null || merchant.getCardNumber() == null );

        if(hasNull) {
            throw new NullMerchantRequestException();
        }

        //map DTO to entities
        List<Merchant> entities = merchants.stream().map(dto -> {
            Merchant merchant = new Merchant();
            merchant.setName(dto.getName());
            merchant.setEmail(dto.getEmail());
            merchant.setBlacklisted(false);
            merchant.setCardNumber(dto.getCardNumber());
            merchant.setCreatedAt(LocalDateTime.now());
            merchant.setBlacklistedAt(null);
            return merchant;
        }).toList();

        //save to database and map to response
        return merchantRepository.saveAll(entities).stream().map(saved -> {
            MerchantResponseDTO response = new MerchantResponseDTO();
            response.setEmail(saved.getEmail());
            response.setName(saved.getName());
            response.setIsBlacklisted(saved.isBlacklisted());
            return response;
        }).toList();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public  void blacklistMerchant(Merchant merchant) {
        merchant.setBlacklisted(true);
        merchant.setBlacklistedAt(LocalDateTime.now());
        merchantRepository.saveAndFlush(merchant);
        log.info("Merchant {} blacklisted and committed to DB", merchant.getId());
    }

//    @Transactional
//    public void clearBlacklist(Merchant merchant) {
//        merchant.setBlacklisted(false);
//        merchant.setBlacklistedAt(null);
//        merchantRepository.save(merchant);
//        log.info("Merchant {} blacklist cleared", merchant.getId());
//    }


}

