package com.servicedesk.ticketservice.config;

import com.servicedesk.ticketservice.model.Ticket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class TicketWebSocketHandler extends TextWebSocketHandler {

    @Autowired private SimpMessagingTemplate messagingTemplate;

    public void sendTicketNotification(String agentId, Ticket ticket) {
        messagingTemplate.convertAndSend("/topic/ticket-notification/"+agentId,ticket);
    }
}
