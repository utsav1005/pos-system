package com.enterprise.pos.dto.response;

import com.enterprise.pos.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponse {
    private String accessToken;
    private String refreshToken;
    private String email;
    private String message;
    private UserDto user;
    private Set<String> roles;

    public AuthResponse(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
