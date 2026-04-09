package com.enterprise.pos.service.serviceImpl;
import com.enterprise.pos.dto.UserDto;
import com.enterprise.pos.dto.request.LoginRequestDto;
import com.enterprise.pos.dto.request.SignupRequestDto;
import com.enterprise.pos.dto.response.AuthResponse;
import com.enterprise.pos.dto.response.SignupAuthResponse;
import com.enterprise.pos.exceptions.UserException;
import com.enterprise.pos.model.User;
import com.enterprise.pos.model.enums.UserRole;
import com.enterprise.pos.repository.UserRepository;
import com.enterprise.pos.security.jwt.JwtProvider;
import com.enterprise.pos.security.jwt.UserPrincipal;
import com.enterprise.pos.service.AuthService;
import com.enterprise.pos.service.UserService;
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
    private final UserService userService;
    private final UserServiceImpl userServiceImpl;

    @Override
    public SignupAuthResponse signup(SignupRequestDto signupRequestDto) {
        if (userRepository.findByEmail(signupRequestDto.getEmail()).isPresent()) {
            throw new UserException("Email already registered!");
        }

        if (UserRole.ROLE_ADMIN.equals(signupRequestDto.getRole())) {
            throw new UserException("You are not allowed to perform this action!");
        }

        User newUser = User.builder()
                .email(signupRequestDto.getEmail())
                .password(passwordEncoder.encode(signupRequestDto.getPassword()))
                .roles(Set.of(UserRole.ROLE_USER))
                .phone(signupRequestDto.getPhone())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .fullName(signupRequestDto.getFullName())
                .build();

        User savedUser = userRepository.save(newUser);

        UserPrincipal principal = UserPrincipal.fromUser(savedUser);
        String jwt =  jwtProvider.generateAccessToken(principal);
        UserDto userDto = modelMapper.map(savedUser, UserDto.class);
        return SignupAuthResponse.builder()
                .user(userDto)
                .accessToken(jwt)
                .message("Registration Successfully done !!!")
                .build();
    }

    @Override
    public AuthResponse login(LoginRequestDto loginRequestDto) {
        //checks in DB user is present is Authenticated or not
        Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequestDto.getEmail(), loginRequestDto.getPassword())
        );
        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();
        User user = modelMapper.map(userService.getUserByEmail(principal.getEmail()) , User.class);
        String jwtAccessToken = jwtProvider.generateAccessToken(principal);
        String jwtRefreshToken = jwtProvider.generateRefreshToken(principal);
        UserDto userDto = UserDto.builder()
                .email(principal.getEmail())
                .fullName(user.getFullName())
                .password(user.getPassword())
                .id(user.getId())
                .createdAt(user.getCreatedAt())
                .lastLogin(LocalDateTime.now())
                .phone(user.getPhone())
                .roles(principal.getAuthorities().stream()
                        .map(GrantedAuthority::getAuthority).collect(Collectors.toSet()))
                .updatedAt(user.getUpdatedAt())
                .build();
        user.setLastLogin(userDto.getLastLogin());
        userRepository.save(user);

        return AuthResponse.builder()
                .accessToken(jwtAccessToken)
                .refreshToken(jwtRefreshToken)
                .user(userDto)
                .message("Login Successfully done!!")
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

