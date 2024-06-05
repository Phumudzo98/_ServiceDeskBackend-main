package com.servicedesk.usersservice.security;

import com.servicedesk.usersservice.repository.AgentAccountRepository;
import com.servicedesk.usersservice.repository.UserAccountRepository;
import com.servicedesk.usersservice.repository.company.AccountRepository;
import com.servicedesk.usersservice.repository.company.AdministratorRepository;
import com.servicedesk.usersservice.service.AuthService;
import io.jsonwebtoken.*;
import io.jsonwebtoken.ExpiredJwtException;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.DisabledException;
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
import java.util.UUID;


@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final String tokenSecret;
    private UserDetailsService userDetailsService;

    private AgentAccountRepository agentAccountRepository;
    private  UserAccountRepository userAccountRepository;
    private final JwtExpiredLogoutHandler jwtExpiredLogoutHandler;
    private AdministratorRepository administratorRepository;
    private AccountRepository accountRepository;
    private final  AuthService authService;

    public JwtFilter(String tokenSecret, UserDetailsService userDetailsService, AgentAccountRepository agentAccountRepository, UserAccountRepository userAccountRepository, JwtExpiredLogoutHandler jwtExpiredLogoutHandler, AdministratorRepository administratorRepository, AccountRepository accountRepository,AuthService authService) {
        this.tokenSecret = tokenSecret;
        this.userDetailsService = userDetailsService;
        this.agentAccountRepository = agentAccountRepository;
        this.userAccountRepository = userAccountRepository;
        this.jwtExpiredLogoutHandler = jwtExpiredLogoutHandler;
        this.administratorRepository = administratorRepository;
        this.authService = authService;
        this.accountRepository = accountRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(StringUtils.isEmpty(header) || !header.startsWith("Bearer ")){
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.replace("Bearer ", "");

        try {
            byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
            SecretKey signingKey = new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());
            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(token);

            String email = null;
            String accountId = claimsJws.getBody().get("accountId", String.class);

            if (agentAccountRepository.findById(UUID.fromString(accountId)).isPresent()) {
                email = agentAccountRepository.findById(UUID.fromString(accountId)).get().getEmail();
            } else if (userAccountRepository.findById(UUID.fromString(accountId)).isPresent()) {
                email = userAccountRepository.findById(UUID.fromString(accountId)).get().getEmail();
            } else if (administratorRepository.findById(UUID.fromString(accountId)).isPresent()) {
                email = administratorRepository.findById(UUID.fromString(accountId)).get().getEmail();
            } else if (accountRepository.findById(UUID.fromString(accountId)).isPresent()) {
                email = accountRepository.findById(UUID.fromString(accountId)).get().getUsername();
            }

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(email);
                if (validateToken(token)) {
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }

        } catch (ExpiredJwtException e) {
            Claims claims = e.getClaims();
            String email = claims.get("email", String.class);
            authService.logoutUser(email);
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().write("Token expired");
            response.sendRedirect(request.getContextPath() + "/api/auth/logout");
            return;
        }

        filterChain.doFilter(request, response);
    }

    public void logout(HttpServletRequest request, HttpServletResponse response, UserDetails userDetails) {
        try {
            Claims claims = (Claims) request.getAttribute("claims"); // Assuming you set "claims" attribute in the request
            if (claims != null) {
                String email = claims.get("email", String.class); // Extract email from claims
                System.out.println(email);
                authService.logoutUser(email); // Assuming authService has a logoutUser method
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("Token expired");
            }
        } catch (ExpiredJwtException | IOException e) {
            // Handle exception if needed
        } finally {
            SecurityContextHolder.clearContext(); // Clear security context
        }
    }

    public boolean validateToken(String token) {
        byte[] secretKeyBytes = Base64.getEncoder().encode(tokenSecret.getBytes());
        SecretKey signingKey =new SecretKeySpec(secretKeyBytes, SignatureAlgorithm.HS512.getJcaName());

        try {
            Jws<Claims> claimsJws = Jwts.parser()
                    .setSigningKey(signingKey)
                    .parseClaimsJws(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}