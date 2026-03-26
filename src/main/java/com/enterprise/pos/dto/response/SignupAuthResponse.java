package com.enterprise.pos.dto.response;

import com.enterprise.pos.dto.UserDto;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Set;

@Data
@Builder
public class SignupAuthResponse {
    private String accessToken;
    private String message;
    private UserDto user;
}
