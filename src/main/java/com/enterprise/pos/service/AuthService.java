package com.enterprise.pos.service;

import com.enterprise.pos.dto.UserDto;
import com.enterprise.pos.dto.request.LoginRequestDto;
import com.enterprise.pos.dto.request.SignupRequestDto;
import com.enterprise.pos.dto.response.AuthResponse;
import com.enterprise.pos.dto.response.SignupAuthResponse;

public interface AuthService {

    SignupAuthResponse signup(SignupRequestDto signupRequestDto);
    AuthResponse login(LoginRequestDto loginRequestDto);
    AuthResponse refreshToken(String refreshToken);


}
