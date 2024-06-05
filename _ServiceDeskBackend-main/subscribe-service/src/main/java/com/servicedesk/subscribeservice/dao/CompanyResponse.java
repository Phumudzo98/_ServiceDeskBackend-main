package com.servicedesk.subscribeservice.dao;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CompanyResponse {
    private UUID companyId;
    private String companyName;
    private String companyEmail;
    private String contactNumber;
}
