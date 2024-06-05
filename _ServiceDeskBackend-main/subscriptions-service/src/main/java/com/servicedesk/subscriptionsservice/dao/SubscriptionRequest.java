package com.servicedesk.subscriptionsservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SubscriptionRequest {
    private CompanyResponse companyResponse;
    private PackageResponse packageResponse;
}