package com.servicedesk.billingservice.service;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.servicedesk.billingservice.dao.SubscriptionDAO;
import com.servicedesk.billingservice.dao.SuccessfulPayResponse;
import lombok.RequiredArgsConstructor;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RequiredArgsConstructor
@Service
public class BillingService {
    private static final String PAY_FAST_API_URL = "https://sandbox.payfast.co.za/eng/process";
    private static final String PAYMENT_SUCCESS_URL = "http://localhost:8080/api/billing/payment/success/";
    private static final String MERCHANT_ID = "10029164";
    private static final String MERCHANT_KEY = "2oki9md2y1wjc";
    private static final Integer EXPIRE_MIN = 10;
    private LoadingCache<String, SubscriptionDAO> subscriptionDetailsCache;
    private final WebClient.Builder webClientBuilder;

    public String initiatePayment(SubscriptionDAO subscriptionDAO) throws IOException{
        String paymentProcessUrl="";

        HttpPost post = new HttpPost(PAY_FAST_API_URL);
        List<NameValuePair> params = new ArrayList<>();

        subscriptionDetailsCache= CacheBuilder.newBuilder()
                                    .expireAfterWrite(EXPIRE_MIN, TimeUnit.MINUTES)
                                            .build(new CacheLoader<String, SubscriptionDAO>() {
                                                @Override
                                                public SubscriptionDAO load(String s) throws Exception {
                                                    return null;
                                                }
                                            });

        subscriptionDetailsCache.put(subscriptionDAO.getCompanyResponse().getCompanyId().toString(),subscriptionDAO);

        System.out.println("Here "+subscriptionDetailsCache);

        params.add(new BasicNameValuePair("merchant_id", MERCHANT_ID));
        params.add(new BasicNameValuePair("merchant_key", MERCHANT_KEY));
        params.add(new BasicNameValuePair("amount", String.format("%.2f", subscriptionDAO.getPackageResponse().getPrice())));
        params.add(new BasicNameValuePair("item_name", "Package_"+subscriptionDAO.getPackageResponse().getPackageName()));
        params.add(new BasicNameValuePair("return_url", PAYMENT_SUCCESS_URL+subscriptionDAO.getCompanyResponse().getCompanyId()));
        post.setEntity(new UrlEncodedFormEntity(params));

        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(post);
        HttpEntity responseEntity = response.getEntity();

        String responseBody = EntityUtils.toString(responseEntity);

        Pattern pattern = Pattern.compile("<a href=\"(.*?)\">");
        Matcher matcher = pattern.matcher(responseBody);
        if (matcher.find()) {
            paymentProcessUrl = matcher.group(1);
        }

        //Global.paymentId=extractPaymentId(paymentProcessUrl);
        return paymentProcessUrl;
    }

    private String extractPaymentId(String url) {
        String[] parts = url.split("/");
        return parts[parts.length - 1];
    }

    public SubscriptionDAO getSubscriptionDAOByKey(String key){
        return subscriptionDetailsCache.getIfPresent(key);
    }

    public String handlePaySuccess(String companyId) {
        SubscriptionDAO subscriptionDAO=getSubscriptionDAOByKey(companyId);

        SuccessfulPayResponse successfulPayResponse =SuccessfulPayResponse.builder()
                .isSuccessful(true)
                .companyResponse(subscriptionDAO.getCompanyResponse())
                .packageResponse(subscriptionDAO.getPackageResponse())
                .build();

        return webClientBuilder.build().post()
                .uri("http://subscribe-service/api/subscribe/store")
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(Mono.just(successfulPayResponse), SuccessfulPayResponse.class)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}
