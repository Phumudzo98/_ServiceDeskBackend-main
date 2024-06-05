package com.servicedesk.accountservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    private String companyName;
    private String companyEmail;
    private String contactNumber;
    private String password;
}
