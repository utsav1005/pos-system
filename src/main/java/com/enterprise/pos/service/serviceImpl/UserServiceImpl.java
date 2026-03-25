package com.enterprise.pos.service.serviceImpl;

import com.enterprise.pos.exceptions.ResourceNotFoundException;
import com.enterprise.pos.model.User;
import com.enterprise.pos.repository.UserRepository;
import com.enterprise.pos.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}

