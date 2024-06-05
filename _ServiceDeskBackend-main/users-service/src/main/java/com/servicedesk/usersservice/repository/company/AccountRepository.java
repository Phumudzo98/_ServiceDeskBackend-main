package com.servicedesk.usersservice.repository.company;

import org.springframework.data.jpa.repository.JpaRepository;
import com.servicedesk.usersservice.model.company.Account;

import java.util.Optional;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {

    Optional<Account> findByUsername(String username);


}
