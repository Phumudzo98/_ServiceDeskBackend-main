package com.servicedesk.billingservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PackageResponse {
    private String packageId;
    private String packageName;
    private double price;
    private String description;
    private Integer period;
}