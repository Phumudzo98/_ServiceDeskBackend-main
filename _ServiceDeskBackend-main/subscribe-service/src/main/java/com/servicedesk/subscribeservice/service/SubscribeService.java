package com.servicedesk.subscribeservice.service;

import com.servicedesk.subscribeservice.Global;
import com.servicedesk.subscribeservice.dao.*;

import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.http.HttpHeaders;

import reactor.core.publisher.Mono;
@Service
@RequiredArgsConstructor
public class SubscribeService {
    private final WebClient.Builder webClientBuilder;

    public String storeSubscription(PaymentResponse paymentResponse) {
        return paymentResponse.isSuccessful() ?
                webClientBuilder.build()
                        .post()
                        .uri("http://subscriptions-service/api/subscription/post-subscription")
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(Mono.just(paymentResponse), PaymentResponse.class)
                        .retrieve()
                        .bodyToMono(String.class)
                        .block()
        :
        "Payment Failed";
    }

    public SubscribeResponse subscribe(String companyId,String packageId){

        if(this.retrieveSubscription(companyId)!=null){

            return null;
        }else{
            SubscriptionRequest subscriptionRequest = SubscriptionRequest.builder().companyResponse(this.retrieveCompanyInformation(companyId)).packageResponse(this.retrievePackageInformation(packageId)).build();

            String url=webClientBuilder.build()
                    .post()
                    .uri("http://billing-service/api/billing/payment")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .body(Mono.just(subscriptionRequest), SubscriptionRequest.class)
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();

            return SubscribeResponse.builder().paymentUrl(url).subscriptionRequest(subscriptionRequest).build();
        }
    }

    private CompanyResponse retrieveCompanyInformation(String companyId){
        return  webClientBuilder.build().get()
                .uri("http://subscriber-account-service/api/subscriber/get-company/"+companyId)
                .retrieve()
                .bodyToMono(CompanyResponse.class)
                .block();
    }
    private PackageResponse retrievePackageInformation(String packageId){
        return webClientBuilder.build().get()
                .uri("http://package-service/api/packages/get/one/"+packageId).retrieve()
                .bodyToMono(PackageResponse.class)
                .block();
    }

    private SubscriptionResponse retrieveSubscription(String companyId) {
        return webClientBuilder.build().get()
                .uri("http://subscriptions-service/api/subscription/get-subscription/"+companyId).retrieve()
                .bodyToMono(SubscriptionResponse.class)
                .block();
    }
}