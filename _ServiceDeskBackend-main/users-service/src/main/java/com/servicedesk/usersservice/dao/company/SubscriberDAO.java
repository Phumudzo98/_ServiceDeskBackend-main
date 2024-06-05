package com.servicedesk.usersservice.dao.company;

import  com.servicedesk.usersservice.model.company.PostalAddress;
import  com.servicedesk.usersservice.model.company.PhysicalAddress;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriberDAO {
    private String companyName;
    private String companyEmail;
    private String contactNumber;
    private String password;
    private PhysicalAddress physicalAddress;
    private PostalAddress postalAddress;
}
