package com.example.VerveGaurd.controller;

import com.example.VerveGaurd.dto.AdminResponseDTO;
import com.example.VerveGaurd.dto.BlacklistedMerchantsResponse;
import com.example.VerveGaurd.dto.CreateAdminRequestDto;
import com.example.VerveGaurd.resposne.ApiResponse;
import com.example.VerveGaurd.resposne.PagedResponse;
import com.example.VerveGaurd.service.AdminService;
import com.example.VerveGaurd.service.TransactionService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
public class AdminController {
    private final AdminService adminService;

    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/flagged")
    public ResponseEntity<ApiResponse<List<BlacklistedMerchantsResponse>>> fetchBlacklistedMerchants() {
        List<BlacklistedMerchantsResponse> blacklistedMerchants = adminService.fetchBlacklistedMerchants();

        return ResponseEntity.status(HttpStatus.OK).body(ApiResponse.success(blacklistedMerchants, "Blacklisted merchants fetched successfully"));
    }



    @GetMapping("")
    public ResponseEntity<ApiResponse<PagedResponse<AdminResponseDTO>>> fetchAllAdmins( @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int size) {
        PagedResponse<AdminResponseDTO> admins = adminService.getAdmins(page, size);

        return ResponseEntity.ok().body(ApiResponse.success(admins, "List of admins fetched successfully"));
    }



}