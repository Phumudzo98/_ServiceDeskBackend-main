package com.servicedesk.subscriptionsservice.controller;

import com.servicedesk.subscriptionsservice.dao.SubscriptionRequest;
import com.servicedesk.subscriptionsservice.dao.SubscriptionResponse;
import com.servicedesk.subscriptionsservice.service.SubscriptionsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscription")
public class subscriptionsServiceController {

    private final SubscriptionsService subscriptionsService;

    @PostMapping(value = "/post-subscription")
    public String postSubscription(@RequestBody SubscriptionRequest subscriptionRequest){
        return  subscriptionsService.storeSubscription(subscriptionRequest);
    }

    @GetMapping(value = "/get-subscription/{companyId}")
    public SubscriptionResponse getSubscription(@PathVariable("companyId") String companyId){
        return subscriptionsService.getActiveSubscription(UUID.fromString(companyId));
    }
}