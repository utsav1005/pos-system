package com.enterprise.pos.service.serviceImpl;

import com.enterprise.pos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl {

    private final UserRepository userRepository;


}
