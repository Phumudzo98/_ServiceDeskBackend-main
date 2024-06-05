package com.servicedesk.ticketservice.dao;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ticket {
    private int Resolved;
    private int MonthlyTotal;
}