package com.enterprise.pos.model;

import com.enterprise.pos.model.enums.UserRole;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String fullName;

    @Column(nullable = false ,  unique = true)
    @Email(message = "Email should be valid")
    private String email;

    @Column(nullable = false , unique = true)
    private String password;

    @Column(nullable = false ,  unique = true)
    private String phone;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime lastLogin;



}
