package com.servicedesk.chatservice.service;

import com.servicedesk.chatservice.dao.MessageRequest;
import com.servicedesk.chatservice.dao.MessageRespond;
import com.servicedesk.chatservice.dao.UserResponse;
import com.servicedesk.chatservice.model.Message;
import com.servicedesk.chatservice.repository.MessageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class ChatService {

    private final WebClient.Builder webClientBuilder;
    private final MessageRepository messageRepository;

    public List<MessageRespond> getMessages(UUID ticketId){
        List<Message> messages =messageRepository.findByTicketId(ticketId);
        return messages.stream().map(this::mapToMessageRespond).toList();
    }

    private MessageRespond mapToMessageRespond(Message message){
        return MessageRespond.builder()
                .content(message.getContent())
                .sender(message.getSender())
                .timestamp(message.getTimestamp())
                .build();
    }

    public MessageRespond sendMessage(MessageRequest request){

        Message message = Message.builder()
                .content(request.getContent())
                .timestamp(LocalDateTime.now())
                .sender(request.getSender())
                .ticketId(request.getTicketId()).build();
        Message saveMessage = messageRepository.save(message);

        return mapToMessageRespond(saveMessage);
    }

    private UserResponse getUser(String userId){
        try{
            return webClientBuilder.build().get()
                    .uri("http://users-service/api/users/get-user/" + userId)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();
        }catch (WebClientResponseException exception){
            System.out.println(exception.getMessage());
            return null;
        }
    }
}
