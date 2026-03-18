package com.example.VerveGaurd.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "Admin")
public class Admin {
    @Id
    @Column(name = "Id", nullable = false, updatable = false)
    private String Id;

    private String name;

    @Column(nullable = false, unique = true)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String role;

    private LocalDateTime createdAt;

    @PrePersist
    public void generateId() {
        this.Id = "admin-" + UUID.randomUUID().toString();
    }
}
