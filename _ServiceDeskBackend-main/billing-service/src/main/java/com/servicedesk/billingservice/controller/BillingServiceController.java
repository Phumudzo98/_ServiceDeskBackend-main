package com.servicedesk.billingservice.controller;

import com.servicedesk.billingservice.dao.SubscriptionDAO;
import com.servicedesk.billingservice.service.BillingService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/billing")
public class BillingServiceController {
    private final BillingService billingService;

    @PostMapping({"/payment"})
    public String processPayment(@RequestBody SubscriptionDAO subscriptionDAO) throws IOException {
        return this.billingService.initiatePayment(subscriptionDAO);
    }

    @GetMapping({"/payment/success/{companyId}"})
    public String success(@PathVariable("companyId") String companyId){
        return this.billingService.handlePaySuccess(companyId);
    }
}
