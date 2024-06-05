package com.servicedesk.usersservice.dao.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AdministratorResponse {
    private String position;
    private String email;
    private String fullName;
}