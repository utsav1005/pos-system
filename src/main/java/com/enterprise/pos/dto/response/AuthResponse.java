package com.enterprise.pos.dto.response;

import com.enterprise.pos.dto.UserDto;
import com.enterprise.pos.dto.request.LoginRequestDto;
import com.enterprise.pos.model.User;
import com.enterprise.pos.model.enums.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String message;
    private UserDto user;

    public AuthResponse(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}

