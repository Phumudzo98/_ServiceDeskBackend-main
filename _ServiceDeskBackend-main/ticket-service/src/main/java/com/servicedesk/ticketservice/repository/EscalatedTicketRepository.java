package com.servicedesk.ticketservice.repository;

import com.servicedesk.ticketservice.model.EscalatedTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface EscalatedTicketRepository extends JpaRepository<EscalatedTicket, UUID> {
}
