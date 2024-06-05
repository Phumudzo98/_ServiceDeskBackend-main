package com.servicedesk.accountservice.security;

import com.servicedesk.accountservice.repository.AccountRepository;
import com.servicedesk.accountservice.repository.CompanyRepository;
import io.jsonwebtoken.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

public class JwtFilter extends OncePerRequestFilter {
    private final String tokenSecret;
    private UserDetailsService userDetailsService;

    @Autowired
    AccountRepository accountRepository;

    public JwtFilter(String tokenSecret,UserDetailsService userDetailsService) {
        this.tokenSecret = tokenSecret;
        this.userDetailsService=userDetailsService;
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

        JwtParser jwtParser = Jwts.parser().setSigningKey(signingKey);

        Jwt<Header, Claims> parsedToken = jwtParser.parse(token);

        String companyEmail=parsedToken.getBody().get("userEmail",String.class);

        if (companyEmail !=null  && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(companyEmail);
            if(isJwtValid(token)){
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                        null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }

    private boolean isJwtValid(String jwt){
        boolean isValid=true;String subject=null;

        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());

        SecretKey signingKey =new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        JwtParser jwtParser = Jwts.parser().setSigningKey(signingKey);

        try{
            Jwt<Header, Claims> parsedToken = jwtParser.parse(jwt);
            subject = parsedToken.getBody().getSubject();

            if(parsedToken.getBody().getExpiration().before(new Date()))
                isValid=false;

        }catch (Exception e){
            isValid=false;
        }

        if(subject==null||subject.isEmpty())
            isValid=false;

        return isValid;
    }
}