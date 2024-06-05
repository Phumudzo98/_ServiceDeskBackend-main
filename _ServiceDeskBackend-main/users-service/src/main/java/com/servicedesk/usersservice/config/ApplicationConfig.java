package com.servicedesk.usersservice.config;

import com.servicedesk.usersservice.model.AgentAccount;
import com.servicedesk.usersservice.model.ERole;
import com.servicedesk.usersservice.model.UserAccount;
import com.servicedesk.usersservice.model.company.Account;
import com.servicedesk.usersservice.model.company.Administrator;
import com.servicedesk.usersservice.repository.AgentAccountRepository;
import com.servicedesk.usersservice.repository.UserAccountRepository;
import com.servicedesk.usersservice.repository.company.AccountRepository;
import com.servicedesk.usersservice.repository.company.AdministratorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableScheduling
@RequiredArgsConstructor
public class ApplicationConfig {

    private final AgentAccountRepository agentAccountRepository;
    private final UserAccountRepository userAccountRepository;
    private final AccountRepository accountRepository;
    private final AdministratorRepository administratorRepository;

    private User user;

    @Bean
    public UserDetailsService userDetailsService() {
        return email -> {
            if(agentAccountRepository.findByEmail(email).isPresent()){
                AgentAccount agentAccount =agentAccountRepository.findByEmail(email).get();
                 return User.withUsername(agentAccount.getEmail()).password(agentAccount.getPassword())
                        .roles(String.valueOf(ERole.AGENT)).build();
            } else if (userAccountRepository.findByEmail(email).isPresent()) {
                UserAccount userAccount =userAccountRepository.findByEmail(email).get();
                return User.withUsername(userAccount.getEmail()).password(userAccount.getPassword())
                        .roles(String.valueOf(ERole.END_USER)).build();
            } else if (accountRepository.findByUsername(email).isPresent()) {
                Account account = accountRepository.findByUsername(email).get();
                return User.withUsername(account.getUsername()).password(account.getPassword())
                        .roles(String.valueOf(ERole.COMPANY)).build();
            } else if (administratorRepository.findByEmail(email).isPresent()) {
                Administrator administrator = administratorRepository.findByEmail(email).get();
                return User.withUsername((administrator.getEmail())).password(administrator.getPassword())
                        .roles(String.valueOf(ERole.ADMIN)).build();
            }
            return null;
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