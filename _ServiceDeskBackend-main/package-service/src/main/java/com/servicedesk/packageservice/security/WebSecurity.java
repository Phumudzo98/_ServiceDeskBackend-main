package com.servicedesk.packageservice.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class WebSecurity {
    @Value("${gateway.ip}")
    private String ipAddress;

    @Value("${token.secret}")
    private String tokenSecret;

    @Bean
    protected SecurityFilterChain configure(HttpSecurity httpSecurity) throws Exception{

        httpSecurity.csrf().disable();
        httpSecurity.authorizeHttpRequests()
                .requestMatchers(HttpMethod.GET,"/api/packages/get/**").access(new WebExpressionAuthorizationManager("hasIpAddress('"+ipAddress+"')"))
                .requestMatchers(HttpMethod.POST,"/api/packages/post").authenticated()
                .requestMatchers(HttpMethod.PUT,"/api/packages/update").authenticated()
                .and()
                .addFilterBefore(new JwtFilter(tokenSecret), UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests().anyRequest().access(new WebExpressionAuthorizationManager("hasIpAddress('"+ipAddress+"')"))
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        return  httpSecurity.build();
    }
}