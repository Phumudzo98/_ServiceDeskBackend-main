package com.servicedesk.ticketservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketResponse {
    private String category;
    private String description;
    private String ticketId;
    private String status;
    private Date createdAt;
    private String priority;
}