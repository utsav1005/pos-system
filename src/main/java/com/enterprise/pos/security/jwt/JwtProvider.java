package com.enterprise.pos.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Getter
@Service
public class JwtProvider {

    private final SecretKey secretKey;

    public JwtProvider(@Value("${jwt.secretKey}") String secretKey) {
        this.secretKey = Keys.hmacShaKeyFor(secretKey.getBytes());
    }

    public String generateAccessToken(UserPrincipal userPrincipal) {
        return Jwts.builder()
                .subject(String.valueOf(userPrincipal.getId()))
                .signWith(getSecretKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*10))  //10min
                .claim("email" , userPrincipal.getEmail())
                .claim("authorities" ,userPrincipal.getAuthorities()
                        .stream()
                        .map(GrantedAuthority::getAuthority)
                        .toList()
                )
                .compact();
    }

    public String generateRefreshToken(UserPrincipal userPrincipal){
        return Jwts.builder()
                .subject(userPrincipal.getUsername())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000L * 60 * 60 * 24 * 7)) //7days
                .signWith(getSecretKey())
                .compact();
    }

    public String getEmailFromToken(String jwt){
        jwt = jwt.substring(7);
        Claims claims = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(jwt)
                .getPayload();
        return String.valueOf(claims.get("email"));
    }

}
