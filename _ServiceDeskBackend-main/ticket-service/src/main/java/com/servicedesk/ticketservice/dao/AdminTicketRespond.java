package com.servicedesk.ticketservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminTicketRespond {
    private String category;
    private String loggedBy;
    private String ticketId;
    private String status;
    private Date createdAt;
    private String priority;
    private String assignedTo;
}
