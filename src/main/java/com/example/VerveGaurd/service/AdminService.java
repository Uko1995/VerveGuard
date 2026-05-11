package com.example.VerveGaurd.service;

import com.example.VerveGaurd.dto.BlacklistedMerchantsResponse;
import com.example.VerveGaurd.dto.AdminResponseDTO;
import com.example.VerveGaurd.exception.AdminNotFoundException;
import com.example.VerveGaurd.model.Admin;
import com.example.VerveGaurd.repository.AdminRepository;
import com.example.VerveGaurd.repository.TransactionRepository;
import com.example.VerveGaurd.resposne.PagedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminService {
    private final TransactionRepository transactionRepository;
    private final AdminRepository adminRepository;


    public AdminService(TransactionRepository transactionRepository, AdminRepository adminRepository) {
        this.transactionRepository = transactionRepository;
        this.adminRepository = adminRepository;
    }

    public List<BlacklistedMerchantsResponse> fetchBlacklistedMerchants(){
        return transactionRepository.findAllFlaggedWithMerchantDetails();
    }



    public PagedResponse<AdminResponseDTO> getAdmins(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Admin> PagedAdmins = adminRepository.findAll(pageable);

        if (PagedAdmins.isEmpty()) {
            throw new AdminNotFoundException("There are no records available for admins");
        }

        List<AdminResponseDTO> admins = PagedAdmins.getContent().stream().map(this::mapToResponse).toList();

        return PagedResponse.<AdminResponseDTO>builder()
                .content(admins)
                .totalElements(PagedAdmins.getTotalElements())
                .currentPage(PagedAdmins.getNumber())
                .isFirst(PagedAdmins.isFirst())
                .isLast(PagedAdmins.isLast())
                .totalPages(PagedAdmins.getTotalPages())
                .pageSize(PagedAdmins.getSize())
                .build();



    }

    private AdminResponseDTO mapToResponse(Admin admin) {
        AdminResponseDTO response = new AdminResponseDTO();
        response.setName(admin.getName());
        response.setEmail(admin.getEmail());
        response.setRole(admin.getRole());

        return response;
    }
}
