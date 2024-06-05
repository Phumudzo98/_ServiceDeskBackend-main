package com.servicedesk.usersservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private UUID accountId;
    private String fullName;
    private String email;
    private String position;
    private String role;
    private String status;
    private String statusAgent;
    private  String lastName;
    private  String contactNumber;

}