package com.servicedesk.billingservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionDAO {
    private CompanyResponse companyResponse;
    private PackageResponse packageResponse;
}
