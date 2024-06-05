package com.servicedesk.ticketservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
@Data
@Entity
@Table(name = "Ticket")
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class Ticket implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ticket_id")
    private UUID ticketId;
    @Column(name = "category",nullable = false)
    private String category;
    @Column(name = "priority",nullable = false)
    private String priority;
    @Column(name = "description",nullable = false)
    @Size(max = 30000000)
    private String description;
    @Column(name = "requested_by",nullable = false)
    private UUID customerUserId;
    @Column(name = "assigned_to",nullable = false)
    private UUID customerAgentId;
    @Column(name = "company_id",nullable = false)
    private UUID companyId;
    @Column(name = "created_at",nullable = false)
    private Date createdAt;
    @Column(name = "updated_at")
    private Date updateAt;
    @Column(name="status",nullable = false)
    private String status;
    @OneToOne(mappedBy = "ticket",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private TicketResolution ticketResolution;
    @OneToOne(mappedBy = "ticket",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private EscalatedTicket escalatedTicket;
    @OneToOne(mappedBy = "ticket",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private InProgressTicket inProgressTicket;
}