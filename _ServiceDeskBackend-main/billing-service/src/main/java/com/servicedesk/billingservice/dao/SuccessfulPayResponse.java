package com.servicedesk.billingservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SuccessfulPayResponse {
    private boolean isSuccessful;
    private CompanyResponse companyResponse;
    private PackageResponse packageResponse;
}
