package com.servicedesk.accountservice.repository;

import com.servicedesk.accountservice.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Optional<Company> findByCompanyEmail(String companyName);
}
