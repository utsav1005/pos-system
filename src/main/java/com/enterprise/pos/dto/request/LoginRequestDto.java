package com.enterprise.pos.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class LoginRequestDto {

    @Email(message = "email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Email is required")
    @Size(min = 6)
    private String password;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastLogin;
}
