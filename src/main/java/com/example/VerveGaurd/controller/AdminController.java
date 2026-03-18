package com.example.VerveGaurd.controller;

import com.example.VerveGaurd.dto.BlacklistedMerchantsResponse;
import com.example.VerveGaurd.service.AdminService;
import com.example.VerveGaurd.service.TransactionService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/flagged")
    public ResponseEntity<List<BlacklistedMerchantsResponse>> fetchBlacklistedMerchants() {
        return ResponseEntity.ok(adminService.fetchBlacklistedMerchants());
    }
}
