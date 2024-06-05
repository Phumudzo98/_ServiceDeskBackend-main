package com.servicedesk.subscribeservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SubscriptionResponse {
    private UUID subscriptionId;
    private UUID companyId;
    private String packageName;
    private String status;
}
