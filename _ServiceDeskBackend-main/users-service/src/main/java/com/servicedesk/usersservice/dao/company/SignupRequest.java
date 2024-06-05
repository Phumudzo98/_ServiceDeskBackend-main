package com.servicedesk.usersservice.dao.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignupRequest {
    //Company info
    private String companyName;
    private String companyEmail;
    private String contactNumber;
    private String password;

    //Administrator info
    private String fullName;
    private String email;
    private String adminContactNumber;
    private String lastName;
    private String position;
    private String adminPassword;
    private String image;
}
