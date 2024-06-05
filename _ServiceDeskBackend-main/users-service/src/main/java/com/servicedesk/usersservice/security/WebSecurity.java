package com.servicedesk.usersservice.security;

import com.servicedesk.usersservice.repository.AgentAccountRepository;
import com.servicedesk.usersservice.repository.UserAccountRepository;
import com.servicedesk.usersservice.repository.company.AccountRepository;
import com.servicedesk.usersservice.repository.company.AdministratorRepository;
import com.servicedesk.usersservice.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity {

    @Value("${token.secret}")
    private String tokenSecret;

    @Value("${ticket-service.ip}")
    private String ticketServiceIp;

    @Value("${gateway.ip}")
    private String gatewayIp;

    @Autowired
    private UserDetailsService userDetailsService;
@Autowired
private  JwtExpiredLogoutHandler jwtExpiredLogoutHandler;
    @Autowired
    private AgentAccountRepository agentAccountRepository;

    @Autowired
    private UserAccountRepository userAccountRepository;

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private AccountRepository accountRepository;
@Autowired
private  AuthService authService;
    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{

        httpSecurity.csrf().disable();
        httpSecurity.authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST,"/api/users/register-users/**").authenticated()
                .requestMatchers(HttpMethod.GET,"/api/users/get-agents/**").access(new WebExpressionAuthorizationManager("hasIpAddress('"+ticketServiceIp+"')"))
                .requestMatchers(HttpMethod.GET,"/api/users/get-users/**").authenticated()
                .requestMatchers(HttpMethod.PUT,"/api/users/change-password/**").authenticated()
                .requestMatchers(HttpMethod.POST,"/api/users/verifyOldPassword").authenticated()
                .requestMatchers(HttpMethod.PUT,"/api/users/reset-password/**").authenticated()
                .requestMatchers(HttpMethod.POST,"/api/users/changeAgentStatus").authenticated()
                .requestMatchers(HttpMethod.POST,"/api/users/updateProfile").authenticated()
                .requestMatchers(HttpMethod.POST,"/api/users/changePassword").authenticated()
                .requestMatchers(HttpMethod.POST,"/api/users/changeUserStatus").authenticated()
                .requestMatchers(HttpMethod.GET,"/api/users/displayProfileImage").authenticated()
                .requestMatchers(HttpMethod.GET,"/api/users/get-user/**").access(new WebExpressionAuthorizationManager("hasIpAddress('"+gatewayIp+"')"))
                .requestMatchers("/api/company/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/users/get-user/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/users/all-users/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/users/get-users-data/**").permitAll()
                .requestMatchers(HttpMethod.GET,"/api/users/get-agent/**").access(new WebExpressionAuthorizationManager("hasIpAddress('"+ticketServiceIp+"')"))
                .requestMatchers(HttpMethod.POST,"/api/auth/**").access(new WebExpressionAuthorizationManager("hasIpAddress('"+gatewayIp+"')")).and()

                .addFilterBefore(new JwtFilter(tokenSecret,userDetailsService,agentAccountRepository,userAccountRepository,jwtExpiredLogoutHandler,administratorRepository,accountRepository,authService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return  httpSecurity.build();
    }
}