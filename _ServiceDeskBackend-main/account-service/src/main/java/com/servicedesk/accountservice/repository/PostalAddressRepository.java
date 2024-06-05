package com.servicedesk.accountservice.repository;

import com.servicedesk.accountservice.model.PostalAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostalAddressRepository extends JpaRepository<PostalAddress, UUID> {
}
