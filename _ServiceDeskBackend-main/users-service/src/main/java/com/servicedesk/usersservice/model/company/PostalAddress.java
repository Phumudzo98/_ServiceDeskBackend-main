package com.servicedesk.usersservice.model.company;

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
@Table(name = "PostalAddress")
public class PostalAddress implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID postalAddressId;
    @Column(name = "street_address", nullable = false)
    private String streetAddress;
    @Column(name = "unit_or_apartment_number", nullable = true)
    private String unitOrApartmentNumber;
    @Column(name = "city", nullable = false,unique = false)
    private String city;
    @Column(name = "state_or_province", nullable = false)
    private String stateOrProvince;
    @Column(name="postal_or_zipcode",nullable = false)
    private String postalOrZipcode;
    @Column(name="country",nullable = false)
    private String country;
    @OneToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name="companyId",nullable = false)
    @JsonIgnore
    private Company company;
}