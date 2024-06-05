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
@Table(name = "InProgress_Ticket")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class InProgressTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ticket_inProgress_id")
    private UUID ticketResolutionId;
    @Size(max = 3000000)
    @Column(name = "reason")
    private String reason;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="ticket_id",nullable = false)
    @JsonIgnore
    private Ticket ticket;
}
