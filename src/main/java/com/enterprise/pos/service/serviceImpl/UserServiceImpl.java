package com.enterprise.pos.service.serviceImpl;

import com.enterprise.pos.dto.UserDto;
import com.enterprise.pos.exceptions.ResourceNotFoundException;
import com.enterprise.pos.exceptions.UnAuthorizedException;
import com.enterprise.pos.exceptions.UserException;
import com.enterprise.pos.model.User;
import com.enterprise.pos.repository.UserRepository;
import com.enterprise.pos.security.jwt.JwtProvider;
import com.enterprise.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final JwtProvider  jwtProvider;
    private final ModelMapper modelMapper;

    @Override
    public UserDto getUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getUserFromJwtToken(String token) {
        String email = jwtProvider.getEmailFromToken(token);
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("Invalid Token"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto getCurrentUser() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        if(email == null){
            throw new UnAuthorizedException("User is not authenticated");
        }
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UserException("User not found"));
        return modelMapper.map(user , UserDto.class);
    }

    @Override
    public UserDto getUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return modelMapper.map(user , UserDto.class);
    }

    @Override
    public List<UserDto> getAllUsers() {
        return List.of();
    }
}

