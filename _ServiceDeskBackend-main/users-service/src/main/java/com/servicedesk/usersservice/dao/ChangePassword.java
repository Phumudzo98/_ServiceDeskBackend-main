package com.servicedesk.usersservice.dao;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ChangePassword {
    private String accountId;
    private String password;
    private String role;
}
