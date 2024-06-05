package com.servicedesk.ticketservice.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Entity
@Table(name = "Ticket_Resolution")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TicketResolution {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ticket_resolution_id")
    private UUID ticketResolutionId;

    @Size(max = 3000000)
    @Column(name = "resolution")
    private String resolution;

    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="ticket_id",nullable = false)
    @JsonIgnore
    private Ticket ticket;
    @Column(name = "updated_at")
    private Date updateAt;
}
