package com.enterprise.pos.controller;

import com.enterprise.pos.dto.UserDto;
import com.enterprise.pos.dto.response.UserResponseDto;
import com.enterprise.pos.model.User;
import com.enterprise.pos.service.UserService;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ModelMapper modelMapper;

    @GetMapping("/profile")
    public ResponseEntity<UserResponseDto> getUserProfile(@RequestHeader("Authorization") String jwt){
        return ResponseEntity.ok(modelMapper.map(userService.getUserFromJwtToken(jwt) , UserResponseDto.class));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDto> getUserById(@RequestHeader("Authorization") @PathVariable Long id){
        return ResponseEntity.ok(modelMapper.map(userService.getUserById(id) , UserResponseDto.class));
    }

}
