package com.servicedesk.subscriptionsservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackageResponse {
    private String packageId;
    private String packageName;
    private BigDecimal price;
    private String description;
    private Integer period;
}