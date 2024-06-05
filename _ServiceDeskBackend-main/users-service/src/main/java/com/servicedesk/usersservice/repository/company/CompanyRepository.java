package com.servicedesk.usersservice.repository.company;

import com.servicedesk.usersservice.model.company.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Optional<Company> findByCompanyEmail(String companyName);
}
