package com.servicedesk.apigateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
public class WebSocketConfig implements WebSocketConfigurer {

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler(), "/socket.io")
                .setAllowedOrigins("*"); // Replace with your desired origins
    }

    @Bean
    public WebSocketHandler myWebSocketHandler() {
        return new WebSocketHandler();
    }
}
