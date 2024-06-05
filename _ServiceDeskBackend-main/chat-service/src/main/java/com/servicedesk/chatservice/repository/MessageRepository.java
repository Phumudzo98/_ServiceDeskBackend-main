package com.servicedesk.chatservice.repository;

import com.servicedesk.chatservice.model.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    List<Message> findByTicketId(UUID ticketId);
}
