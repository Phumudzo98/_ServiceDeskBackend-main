package com.servicedesk.ticketservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private  String accountId;
    private String fullName;
    private String email;
    private String position;
    private String role;
}