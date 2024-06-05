package com.servicedesk.chatservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class MessageRequest {
    private UUID sender;
    private String content;
    private UUID ticketId;
}
