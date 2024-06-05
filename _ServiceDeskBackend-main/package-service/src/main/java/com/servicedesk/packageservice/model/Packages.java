package com.servicedesk.packageservice.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.math.BigDecimal;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(value = "packages")
public class Packages implements Serializable {
    @Id
    private String packageId;
    private String packageName;
    private double price;
    private String description;
    private Integer period;
    private Integer userLimit;
}
