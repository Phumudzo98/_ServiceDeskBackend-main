package com.servicedesk.usersservice.repository;


import com.servicedesk.usersservice.model.AgentAccount;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AgentAccountRepository extends JpaRepository<AgentAccount, UUID> {
    Optional<AgentAccount> findByEmail(String email);
    List<AgentAccount> findByCompanyId(UUID companyId);
}
