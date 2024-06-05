package com.servicedesk.subscriptionsservice.service;

import com.servicedesk.subscriptionsservice.model.Subscription;
import com.servicedesk.subscriptionsservice.repository.SubscriptionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class SubscriptionMonitorService {
    private final SubscriptionsRepository subscriptionsRepository;

    @Scheduled(cron = "0 0 0 * * *")
    public void monitorSubscriptions(){

        List<Subscription> subscriptions = subscriptionsRepository.findAll();

        for(Subscription subscription:subscriptions){
            if(subscription.getEndDate().after(new Date())){
                subscription.setStatus("Expired");
                subscriptionsRepository.save(subscription);
            }
        }
    }
}