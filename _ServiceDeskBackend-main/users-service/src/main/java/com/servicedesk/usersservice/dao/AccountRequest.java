package com.servicedesk.usersservice.dao;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountRequest {
    private String fullName;
    private String email;
    private String contactNumber;
    private String lastName;
    private String position;
    private String role;
    private String status;
    private String statusAgent;
}