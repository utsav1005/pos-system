package com.enterprise.pos.config;

import com.enterprise.pos.security.jwt.JwtValidtor;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import java.util.Arrays;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.requestMatchers("/api/super-admin/**").hasRole("ADMIN")
                                .requestMatchers("/api/**").authenticated() //wrong JWT ACCESS DENIED
                                .anyRequest()
                                .permitAll()
                        //this filter Authenticated User using jwt Token
                ).addFilterBefore(new JwtValidtor(), UsernamePasswordAuthenticationFilter.class)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()));
        return httpSecurity.build();
    }
    private CorsConfigurationSource corsConfigurationSource() {
        return   request -> {
            CorsConfiguration corsConfiguration = new CorsConfiguration();
            corsConfiguration.setAllowedOrigins(
                    Arrays.asList(
                            "http://localhost:3000",
                            "http://localhost:5173"
                    )
            );
            corsConfiguration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
            corsConfiguration.setAllowedHeaders(List.of("*"));
            corsConfiguration.setAllowCredentials(true);
            corsConfiguration.setExposedHeaders(List.of("Authorization"));
            corsConfiguration.setMaxAge(3600L);
            return corsConfiguration;
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
