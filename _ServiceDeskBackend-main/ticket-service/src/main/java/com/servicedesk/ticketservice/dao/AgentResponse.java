package com.servicedesk.ticketservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AgentResponse {
    private UUID accountId;
    private String fullName;
    private String status;
    private String email;
    private boolean isOnLeave;
}