package com.servicedesk.packageservice.security;

import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Base64;

public class JwtFilter extends OncePerRequestFilter {
    private final String tokenSecret;

    public JwtFilter(String tokenSecret) {
        this.tokenSecret = tokenSecret;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.isEmpty(header) || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token =header.replace("Bearer ", "");

        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());

        SecretKey signingKey =new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        JwtParser jwtParser = Jwts.parser()
                .setSigningKey(signingKey);

        try {
            Jwt<Header, Claims> parsedToken = jwtParser.parse(token);
        }catch (JwtException e){
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
