package com.servicedesk.usersservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data

@AllArgsConstructor
@NoArgsConstructor
public class LoginRequest extends UserResponse {
    private String email;
    private String password;
}