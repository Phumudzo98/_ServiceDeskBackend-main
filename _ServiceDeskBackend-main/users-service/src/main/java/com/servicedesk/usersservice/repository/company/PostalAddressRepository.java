package com.servicedesk.usersservice.repository.company;

import com.servicedesk.usersservice.model.company.PostalAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PostalAddressRepository extends JpaRepository<PostalAddress, UUID> {
}
