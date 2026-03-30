package com.enterprise.pos.dto.response;

import com.enterprise.pos.dto.UserDto;
import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class SignupAuthResponse {
    private String accessToken;
    private String message;
    private UserDto user;
}
