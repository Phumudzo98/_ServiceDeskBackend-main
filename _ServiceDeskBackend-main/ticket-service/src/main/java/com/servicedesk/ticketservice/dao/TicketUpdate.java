package com.servicedesk.ticketservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketUpdate {
    private String ticketId;
    private String status;
    private String updateMessage;
    private String escalatedToAgentId;
}