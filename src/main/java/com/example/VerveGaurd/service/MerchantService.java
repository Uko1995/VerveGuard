package com.example.VerveGaurd.service;

import com.example.VerveGaurd.dto.MerchantRequestDTO;
import com.example.VerveGaurd.dto.MerchantResponseDTO;
import com.example.VerveGaurd.exception.NullMerchantRequestException;
import com.example.VerveGaurd.model.Merchant;
import com.example.VerveGaurd.repository.MerchantRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class MerchantService {
    private final MerchantRepository  merchantRepository;

    public MerchantService(MerchantRepository merchantRepository) {
        this.merchantRepository = merchantRepository;
    }

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
}
