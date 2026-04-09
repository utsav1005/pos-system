package com.enterprise.pos.dto.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UserResponseDto {
        private Long id;
        private String fullName;
        private String email;
        private String phone;
        private LocalDateTime createdAt;
        private LocalDateTime updatedAt;
}
