package com.servicedesk.accountservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "Company")
public class Company implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID companyId;
    @Column(name = "company_name", nullable = false,unique = true)
    private String companyName;
    @Column(name = "company_email", nullable = false,unique = true)
    private String companyEmail;
    @Column(name = "company_number", nullable = false,unique = true)
    private String contactNumber;
    @OneToOne(mappedBy = "company",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private PostalAddress postalAddress;
    @OneToOne(mappedBy = "company",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private PhysicalAddress physicalAddress;
    @OneToOne(mappedBy = "company",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private Account account;
    @OneToOne(mappedBy = "company",fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    @JsonIgnore
    private Administrator administrator;
}