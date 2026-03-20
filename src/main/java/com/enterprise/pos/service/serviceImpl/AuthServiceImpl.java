package com.enterprise.pos.service.serviceImpl;
import com.enterprise.pos.dto.UserDto;
import com.enterprise.pos.dto.request.LoginRequestDto;
import com.enterprise.pos.dto.request.SignupRequestDto;
import com.enterprise.pos.dto.response.AuthResponse;
import com.enterprise.pos.exceptions.UserException;
import com.enterprise.pos.model.User;
import com.enterprise.pos.model.enums.UserRole;
import com.enterprise.pos.repository.UserRepository;
import com.enterprise.pos.security.jwt.JwtProvider;
import com.enterprise.pos.security.jwt.UserPrincipal;
import com.enterprise.pos.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final AuthenticationManager authenticationManager;
    private final ModelMapper modelMapper;
    private final UserRepository userRepository;

    @Override
    public AuthResponse signup(SignupRequestDto signupRequestDto) {
        if (userRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
            throw new UserException("Email already registered!");
        }

        if (UserRole.ROLE_ADMIN.equals(signupRequestDto.getRole())) {
            throw new UserException("You are not allowed to perform this action!");
        }

        User newUser = User.builder()
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .role(Set.of(UserRole.ROLE_USER))
                .phone(signupRequestDto.getPhone())
                .fullName(signupRequestDto.getFullName())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .lastLogin(signupRequestDto.getLastLogin())
                .build();

        User savedUser = userRepository.save(newUser);

        UserPrincipal principal = UserPrincipal.fromUser(savedUser);

        String jwt =  jwtProvider.generateAccessToken(principal);
        return AuthResponse.builder()
                .user(modelMapper.map(savedUser, UserDto.class))
                .accessToken(jwt)
                .message("Registered Successfully")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequestDto userDto) {
        //checks in DB user is present is Authenticated or not
        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(userDto.getEmail(), userDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        String jwtAccessToken = jwtProvider.generateAccessToken(principal);
        String jwtRefreshToken = jwtProvider.generateRefreshToken(principal);

        return AuthResponse.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .message("Login Successfully done!!")
                .email(principal.getEmail())
                .roles(principal.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .build();
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        String email = jwtProvider.getEmailFromToken(refreshToken);
        User user = userRepository.findByEmail(email).orElseThrow(() -> new UserException("User not found"));

        UserPrincipal principal =  UserPrincipal.fromUser(user);

        String newRefreshToken =  jwtProvider.generateRefreshToken(principal);

        return new AuthResponse(newRefreshToken);
    }
}

