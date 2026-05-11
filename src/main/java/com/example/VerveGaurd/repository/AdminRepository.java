package com.example.VerveGaurd.repository;

import com.example.VerveGaurd.model.Admin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, String> {

    Page<Admin> findAll(Pageable pageable);

    Boolean existsByEmail(String email);

    Optional<Admin> findByEmail(String email);
}
