package com.servicedesk.usersservice.dao.company;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDAO {
    private String streetAddress;
    private String unitOrApartmentNumber;
    private String city;
    private String stateOrProvince;
    private String postalOrZipcode;
    private String country;
}