package com.servicedesk.ticketservice.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EscalateEvent {
    private String escalatedToFullName;
    private String escalatedToEmail;
    private String escalatedByFullName;
    private String reason;
}
