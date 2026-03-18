package com.enterprise.pos.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Service
public class JwtProvider {

    private final SecretKey secretKey;

    public JwtProvider(@Value("${jwt.secretKey}") String jwtSecretkey) {
        this.secretKey = Keys.hmacShaKeyFor(jwtSecretkey.getBytes());
    }
    public SecretKey getSecretKey() {
        return secretKey;
    }

    public String generateToken(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

        String roles = populateAuthorities(authorities);
        return Jwts.builder()
                .signWith(getSecretKey())
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis()+1000*60*10))  //10min
                .claim("email" , authentication.getName())
                .claim("authorities" , roles)
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

    private String populateAuthorities(Collection<? extends GrantedAuthority> authorities) {
        Set<String> roles = new HashSet<>();

        for(GrantedAuthority grantedAuthority : authorities){
            roles.add(grantedAuthority.getAuthority());
        }
        return String.join(",", roles);

    }


}
