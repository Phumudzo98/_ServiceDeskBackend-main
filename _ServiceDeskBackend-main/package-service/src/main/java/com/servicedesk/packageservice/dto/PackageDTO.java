package com.servicedesk.packageservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PackageDTO {
    private String packageId;
    private String packageName;
    private double price;
    private String description;
    private Integer period;
    private Integer userLimit;
}
