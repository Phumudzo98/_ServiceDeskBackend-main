package com.servicedesk.ticketservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "Ticket_Escalated")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class EscalatedTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ticket_escalation_id")
    private UUID ticketResolutionId;
    @Size(max = 3000000)
    @Column(name = "reason")
    private String reason;
    @Column(name = "escalatedTo")
    private UUID escalatedTo;
    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="ticket_id",nullable = false)
    @JsonIgnore
    private Ticket ticket;
}
