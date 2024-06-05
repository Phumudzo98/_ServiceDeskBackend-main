package com.servicedesk.apigateway.config;

import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class WebSocketHandler extends TextWebSocketHandler {

    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        // This method is called when a new WebSocket connection is established
        // Perform any necessary setup or initialization here
        System.out.println("WebSocket connection established: " + session.getId());
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) throws Exception {
        // This method is called when a text message is received from the client
        String payload = message.getPayload();
        System.out.println("Received message: " + payload);

        // Process the received message

        // Send a response back to the client
        String response = "Hello, client!";
        session.sendMessage(new TextMessage(response));
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) throws Exception {
        // This method is called when a WebSocket connection is closed
        // Perform any necessary cleanup or logging here
        System.out.println("WebSocket connection closed: " + session.getId());
    }

}
