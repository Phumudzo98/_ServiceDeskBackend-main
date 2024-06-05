package com.servicedesk.usersservice.repository.company;

import com.servicedesk.usersservice.model.company.PhysicalAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhysicalRepository extends JpaRepository<PhysicalAddress, UUID> {
}
