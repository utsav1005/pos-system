package com.enterprise.pos.service.serviceImpl;

import com.enterprise.pos.model.User;
import com.enterprise.pos.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomUserServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username).orElseThrow(() ->
                new UsernameNotFoundException("User not found " + username));

        GrantedAuthority grantedAuthority =
                new SimpleGrantedAuthority(user.getRole().toString());
        Collection<GrantedAuthority> authorities = Collections.singletonList(grantedAuthority);

        return new org.springframework.security.core.userdetails.User(
                user.getEmail() , user.getPassword() , authorities
          );
    }
}
