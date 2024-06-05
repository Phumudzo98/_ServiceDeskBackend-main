package com.servicedesk.accountservice.security;

import com.servicedesk.accountservice.model.Account;
import com.servicedesk.accountservice.model.Administrator;
import com.servicedesk.accountservice.model.ERole;
import com.servicedesk.accountservice.repository.AccountRepository;
import com.servicedesk.accountservice.repository.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final AccountRepository accountRepository;
    private final AdministratorRepository administratorRepository;
    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            if(accountRepository.findByUsername(email).isPresent()){
                Account account = accountRepository.findByUsername(email)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
                return User.withUsername(account.getUsername()).password(account.getPassword())
                        .roles(String.valueOf(ERole.SUBSCRIBER)).build();
            }else{
                Administrator administrator = administratorRepository.findByEmail(email)
                        .orElseThrow(()->new UnsupportedOperationException("Not fount"));
                return User.withUsername(administrator.getEmail()).password(administrator.getPassword())
                        .roles(String.valueOf(ERole.ADMIN)).build();
            }
        };
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
