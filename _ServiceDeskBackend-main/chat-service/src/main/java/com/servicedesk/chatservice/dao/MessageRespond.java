package com.servicedesk.chatservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
public class MessageRespond {
    private UUID sender;
    private String content;
    private LocalDateTime timestamp;
}
