package com.servicedesk.chatservice.controller;

import com.servicedesk.chatservice.dao.MessageRequest;
import com.servicedesk.chatservice.dao.MessageRespond;
import com.servicedesk.chatservice.service.ChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class MessageController {

    private final ChatService chatService;

    private final SimpMessagingTemplate simpMessagingTemplate;

    @GetMapping(value = "/get-messages/{ticketId}")
    public List<MessageRespond> getMessages(@PathVariable("ticketId") String ticketId){
        return chatService.getMessages(UUID.fromString(ticketId));
    }

    @MessageMapping("/chat/{ticketId}")
    public void sendMessage(@DestinationVariable String ticketId, MessageRequest request){
        request.setTicketId(UUID.fromString(ticketId));
        MessageRespond messageRespond = chatService.sendMessage(request);
        simpMessagingTemplate.convertAndSend("/topic/messages/"+ticketId,messageRespond);
    }
}

