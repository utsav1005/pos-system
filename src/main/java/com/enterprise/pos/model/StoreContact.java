package com.enterprise.pos.model;

import jakarta.persistence.Embeddable;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreContact {
    private String address;

    @Size(max = 14)
    @Length(max = 14 , message = "Please include country code (ex. +91)")
    @NotBlank(message = "Mobile number is required")
    private String phone;

    @Email(message = "invalid email format")
    @NotBlank(message = "Email is required")
    private String email;

}
