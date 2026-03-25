package com.enterprise.pos.dto.request;


import com.enterprise.pos.model.enums.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SignupRequestDto {

    @Size(min = 1, max = 50)
    private String fullName;

    @Email(message = "email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotNull(message = "phone no in shoulb be 10 digit only")
    @Size(max = 10)
    private String phone;

    @NotBlank(message = "Email is required")
    @Size(min = 6)
    private String password;

    private UserRole role;


    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private LocalDateTime lastLogin;

}
