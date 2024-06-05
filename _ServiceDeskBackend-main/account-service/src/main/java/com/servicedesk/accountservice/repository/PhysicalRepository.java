package com.servicedesk.accountservice.repository;

import com.servicedesk.accountservice.model.PhysicalAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PhysicalRepository extends JpaRepository<PhysicalAddress, UUID> {
}
