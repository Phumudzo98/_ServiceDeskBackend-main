package com.servicedesk.subscriptionsservice.service;

import com.servicedesk.subscriptionsservice.dao.SubscriptionRequest;
import com.servicedesk.subscriptionsservice.dao.SubscriptionResponse;
import com.servicedesk.subscriptionsservice.model.Subscription;
import com.servicedesk.subscriptionsservice.repository.SubscriptionsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SubscriptionsService {

    private final SubscriptionsRepository subscriptionsRepository;
    public String storeSubscription(SubscriptionRequest subscriptionRequest){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        if(subscriptionRequest.getPackageResponse().getPackageName().equals("Trial")){
            calendar.add(calendar.DAY_OF_MONTH,7);
        }else{
            calendar.add(calendar.MONTH,subscriptionRequest.getPackageResponse().getPeriod());
        }

        Date endDate = calendar.getTime();

        Subscription subscription = Subscription.builder()
                .companyId(subscriptionRequest.getCompanyResponse().getCompanyId())
                .packageName(subscriptionRequest.getPackageResponse().getPackageName())
                .startDate(new Date())
                .endDate(endDate)
                .status("Active").build();

        subscriptionsRepository.save(subscription);
        return "Subscription Stored";
    }
    public SubscriptionResponse getActiveSubscription(UUID companyId){
       Subscription subscription = subscriptionsRepository.findSubscriptionByCompanyId(companyId);
       if(subscription!=null){
           return SubscriptionResponse.builder()
                   .subscriptionId(subscription.getSubscriptionId())
                   .status(subscription.getStatus()).build();
       }
        return null;
    }
}