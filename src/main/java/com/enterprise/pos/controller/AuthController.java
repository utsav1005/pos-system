package com.enterprise.pos.controller;

import com.enterprise.pos.dto.request.LoginRequestDto;
import com.enterprise.pos.dto.request.SignupRequestDto;
import com.enterprise.pos.dto.response.AuthResponse;
import com.enterprise.pos.dto.response.SignupAuthResponse;
import com.enterprise.pos.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<SignupAuthResponse> signupUser (@RequestBody SignupRequestDto signupRequestDto) {
        return new ResponseEntity<>(authService.signup(signupRequestDto) , HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> loginUser (
            @RequestBody LoginRequestDto loginRequestDto , HttpServletRequest request , HttpServletResponse response)  {
        AuthResponse authResponse = authService.login(loginRequestDto);
        Cookie cookie = new Cookie("refreshToken" , authResponse.getRefreshToken());
        cookie.setHttpOnly(true);
        cookie.setMaxAge(60*60*24*7);  //7days
        response.addCookie(cookie);
        return new ResponseEntity<>(authResponse , HttpStatus.ACCEPTED);
    }

    @PostMapping("/refresh")
    public AuthResponse refreshUser(@RequestBody String refreshToken) {
        return authService.refreshToken(refreshToken);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logoutUser(HttpServletRequest request , HttpServletResponse response) {
        HttpSession session = request.getSession(false); //request.getSession() returns a session if present then give
        // if session is not present create new Session
        //so that false so if session not present give it null
        if(session != null) {
            session.invalidate();  //logout if session is present
        }
        Cookie cookie = new Cookie("refreshToken" , null);
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return ResponseEntity.ok("logged out successfully");
    }


}
