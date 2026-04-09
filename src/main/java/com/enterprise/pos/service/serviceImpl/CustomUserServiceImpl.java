package com.enterprise.pos.service.serviceImpl;

import com.enterprise.pos.model.User;
import com.enterprise.pos.repository.UserRepository;
import com.enterprise.pos.security.jwt.UserPrincipal;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found " + username));
        log.info("DB Roles  :"+user.getRoles());
        return  UserPrincipal.fromUser(user);

    }


}
