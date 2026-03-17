package com.example.VerveGaurd.controller;

import com.example.VerveGaurd.dto.MerchantRequestDTO;
import com.example.VerveGaurd.dto.MerchantResponseDTO;
import com.example.VerveGaurd.service.MerchantService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/merchants")
public class MerchantController {
    private final MerchantService merchantService;

    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }

    @PostMapping("/batch-save")
    public ResponseEntity<List<MerchantResponseDTO>> batchAdd(@RequestBody List<MerchantRequestDTO> merchants) {
        List<MerchantResponseDTO> response = merchantService.batchAddMerchants(merchants);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
