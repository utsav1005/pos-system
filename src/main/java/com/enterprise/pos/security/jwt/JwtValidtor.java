package com.enterprise.pos.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.crypto.SecretKey;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Component
public class JwtValidtor extends OncePerRequestFilter {

    @Autowired
    @Qualifier("handlerExceptionResolver")
    private HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String jwt = request.getHeader(JwtConstant.JWT_HEADER_NAME);
        if(jwt != null && jwt.startsWith("Bearer ")) {
            jwt = jwt.substring(7);
            try{
                SecretKey secretKey = Keys.hmacShaKeyFor(jwt.getBytes(StandardCharsets.UTF_8));
                Claims claims = Jwts.parser()
                        .verifyWith(secretKey)
                        .build()
                        .parseSignedClaims(jwt)
                        .getPayload();
                String email = String.valueOf(claims.get("email"));
                String roles = String.valueOf(claims.get("authorities")); //Returns Role
                //JWT Based AUTHENTICATION
                List<GrantedAuthority> authorities = AuthorityUtils.commaSeparatedStringToAuthorityList(roles);
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        email,null,authorities);
                //Data inside it:
                //
                //🌐 Remote IP address
                //
                //🧭 Session ID (if exists)
                auth.setDetails(new WebAuthenticationDetails(request));//It attaches extra request-related metadata to your authentication object
                SecurityContextHolder.getContext().setAuthentication(auth);
            }catch(JwtException e){
                //filterchain runs before dispatcher servlet not inside spring MVC
                // so this exception not handled by our Controller Advice so
                // use HandlerResolver to manually forwards exception to spring MVC out @ControllerAdvice can handle it
                handlerExceptionResolver.resolveException(request , response , null , e);
            }
            filterChain.doFilter(request, response);
        }

    }
}
