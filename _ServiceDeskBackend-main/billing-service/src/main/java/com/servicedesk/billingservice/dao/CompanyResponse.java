package com.servicedesk.billingservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CompanyResponse {
    private UUID companyId;
    private String companyName;
    private String companyEmail;
    private String contactNumber;
}
