package com.example.VerveGaurd.controller;

import com.example.VerveGaurd.dto.TransactionRequestDTO;
import com.example.VerveGaurd.dto.TransactionResponseDTO;
import com.example.VerveGaurd.resposne.ApiResponse;
import com.example.VerveGaurd.service.TransactionService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/transactions")
public class TransactionController {
    private final TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @PostMapping("/intercept")
    public ResponseEntity<ApiResponse<TransactionResponseDTO>>  interceptTransaction(@RequestBody  TransactionRequestDTO  dto, HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty()) {
            ipAddress = request.getRemoteAddr();
        }
        TransactionResponseDTO response = transactionService.processTransaction(dto, ipAddress);

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(response, "Transaction intercepted successfully"));
    }
}
