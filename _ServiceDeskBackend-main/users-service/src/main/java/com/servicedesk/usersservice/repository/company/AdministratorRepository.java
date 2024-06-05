package com.servicedesk.usersservice.repository.company;

import com.servicedesk.usersservice.model.company.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AdministratorRepository extends JpaRepository<Administrator, UUID> {
    Optional<Administrator> findByEmail(String email);


    @Query(value = "SELECT * FROM administrator WHERE company_id=:companyId",nativeQuery = true)
    List<Administrator> findByCompanyId(UUID companyId);

    @Query(value = "SELECT * FROM administrator WHERE email=:email",nativeQuery = true)
    Administrator findAdministrator(String email);
}
