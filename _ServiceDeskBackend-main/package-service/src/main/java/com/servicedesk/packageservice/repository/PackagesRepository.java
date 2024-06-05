package com.servicedesk.packageservice.repository;

import com.servicedesk.packageservice.model.Packages;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface PackagesRepository extends MongoRepository<Packages, String> {
    Optional<Object> findByPackageName(String packageName);
}
