package com.servicedesk.usersservice.dao.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private String companyName;
    private String email;
    private String companyId;
    private String fullName;
}