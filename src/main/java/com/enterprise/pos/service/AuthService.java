package com.enterprise.pos.service;

import com.enterprise.pos.dto.UserDto;
import com.enterprise.pos.dto.request.LoginRequestDto;
import com.enterprise.pos.dto.request.SignupRequestDto;
import com.enterprise.pos.dto.response.AuthResponse;

public interface AuthService {

    AuthResponse signup(SignupRequestDto signupRequestDto);
    AuthResponse login(LoginRequestDto loginRequestDto);
    AuthResponse refreshToken(String refreshToken);


}
