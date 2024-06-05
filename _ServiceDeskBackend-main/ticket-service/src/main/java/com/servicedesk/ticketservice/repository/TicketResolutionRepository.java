package com.servicedesk.ticketservice.repository;

import com.servicedesk.ticketservice.model.TicketResolution;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface TicketResolutionRepository extends JpaRepository<TicketResolution, UUID> {
}
