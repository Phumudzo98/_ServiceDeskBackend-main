package com.servicedesk.accountservice.dao;


import com.servicedesk.accountservice.model.PhysicalAddress;
import com.servicedesk.accountservice.model.PostalAddress;
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
