package com.servicedesk.ticketservice.repository;

import com.servicedesk.ticketservice.model.InProgressTicket;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface InProgressTicketRepository  extends JpaRepository<InProgressTicket, UUID> {
}
