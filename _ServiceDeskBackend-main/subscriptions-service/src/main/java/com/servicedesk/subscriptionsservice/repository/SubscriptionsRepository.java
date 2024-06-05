package com.servicedesk.subscriptionsservice.repository;

import com.servicedesk.subscriptionsservice.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SubscriptionsRepository extends JpaRepository<Subscription, UUID> {
    @Query(value="SELECT * FROM subscriptions WHERE status='Active'",nativeQuery = true)
    Subscription findSubscriptionByCompanyId(UUID companyId);
}
