package com.servicedesk.usersservice.repository;

import com.servicedesk.usersservice.model.UserAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserAccountRepository extends JpaRepository<UserAccount, UUID> {
    Optional<UserAccount> findByEmail(String email);
    List<UserAccount> findByCompanyId(UUID companyId);

}
