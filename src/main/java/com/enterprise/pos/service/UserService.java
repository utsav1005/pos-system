package com.enterprise.pos.service;

import com.enterprise.pos.dto.UserDto;
import com.enterprise.pos.model.User;

import java.util.List;

public interface UserService {
    UserDto getUserByEmail(String email);
    UserDto getUserFromJwtToken(String token);
    UserDto getCurrentUser();
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();


}





