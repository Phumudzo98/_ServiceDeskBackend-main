package com.servicedesk.apigateway.security;


import io.jsonwebtoken.*;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    @Value("${token.secret}")
    private String tokenSecret;

    public AuthorizationHeaderFilter(){
        super(Config.class);
    }
    public static class Config{

    }
    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request =exchange.getRequest();

            if(!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION))
                return onError(exchange,"No Authorization Header");

            String authorizationHeader=request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt=authorizationHeader.replace("Bearer","");


            if(!validateToken(jwt))
                return onError(exchange,"Jwt is not valid");

         return chain.filter(exchange);
        };
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err){
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        return response.setComplete();
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