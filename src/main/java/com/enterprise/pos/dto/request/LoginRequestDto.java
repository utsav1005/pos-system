package com.enterprise.pos.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @Email(message = "email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @NotBlank(message = "Email is required")
    @Size(min = 6)
    private String password;

}
