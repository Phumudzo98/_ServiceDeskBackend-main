package com.servicedesk.usersservice.dao.company;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdminRequest {
    private String fullName;
    private String email;
    private String contactNumber;
    private String lastName;
    private String position;
    private String password;
}