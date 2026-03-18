package com.example.VerveGaurd.service;

import com.example.VerveGaurd.dto.BlacklistedMerchantsResponse;
import com.example.VerveGaurd.repository.AdminRepository;
import com.example.VerveGaurd.repository.TransactionRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final TransactionRepository transactionRepository;


    public AdminService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<BlacklistedMerchantsResponse> fetchBlacklistedMerchants(){
        return transactionRepository.findAllFlaggedWithMerchantDetails();
    }
}
