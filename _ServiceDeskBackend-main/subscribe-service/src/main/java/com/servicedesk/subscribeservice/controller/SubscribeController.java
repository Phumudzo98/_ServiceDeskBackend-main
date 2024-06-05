package com.servicedesk.subscribeservice.controller;

import com.servicedesk.subscribeservice.dao.PaymentResponse;
import com.servicedesk.subscribeservice.dao.SubscribeRequest;
import com.servicedesk.subscribeservice.dao.SubscribeResponse;
import com.servicedesk.subscribeservice.service.SubscribeService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/subscribe")
@RequiredArgsConstructor
public class SubscribeController {
    private final SubscribeService subscribeService;


    @PostMapping(value = "/subscribe",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> subscribe(@RequestBody SubscribeRequest subscribeRequest){

        SubscribeResponse subscribeResponse =subscribeService.subscribe(subscribeRequest.getCompanyId(),subscribeRequest.getPackageId());

        if(subscribeResponse==null)
            return ResponseEntity.status(HttpStatus.CONFLICT).body("You already have active subscription");

        return ResponseEntity.ok(subscribeResponse);
    }

    @PostMapping(value = "/store",consumes = MediaType.APPLICATION_JSON_VALUE)
    public String storeSubscription(@RequestBody PaymentResponse request){
        return subscribeService.storeSubscription(request);
    }
}