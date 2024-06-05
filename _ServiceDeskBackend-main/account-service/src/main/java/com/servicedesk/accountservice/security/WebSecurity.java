package com.servicedesk.accountservice.security;

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

    @Value("${gateway.ip}")
    private String ipAddress;

    @Value("${user-service.ip}")
    private String userServiceIp;

    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{

        httpSecurity.csrf().disable();
        httpSecurity.authorizeHttpRequests()
                .requestMatchers(HttpMethod.POST,"/api/subscriber/**").access(new WebExpressionAuthorizationManager("hasIpAddress('"+ipAddress+"')"))
                .requestMatchers(HttpMethod.GET,"/api/subscriber/get-admin/**").access(new WebExpressionAuthorizationManager("hasIpAddress('"+userServiceIp+"')"))
                .requestMatchers(HttpMethod.GET,"/api/subscriber/**").access(new WebExpressionAuthorizationManager("hasIpAddress('"+ipAddress+"')"))
                .anyRequest().authenticated()
                .and()
                .addFilterBefore(new JwtFilter(tokenSecret,userDetailsService), UsernamePasswordAuthenticationFilter.class)
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return  httpSecurity.build();
    }
}