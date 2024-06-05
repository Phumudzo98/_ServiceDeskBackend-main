package com.servicedesk.usersservice.service;

import com.servicedesk.usersservice.dao.SubscriptionResponse;
import com.servicedesk.usersservice.model.AgentAccount;
import com.servicedesk.usersservice.model.UserAccount;
import com.servicedesk.usersservice.repository.AgentAccountRepository;
import com.servicedesk.usersservice.repository.UserAccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
@Service
@RequiredArgsConstructor
@Transactional
public class UserAccountMonitorService {
    private final AgentAccountRepository agentAccountRepository;
    private final UserAccountRepository userAccountRepository;
    private final WebClient.Builder webClientBuilder;

    @Scheduled(cron = "0 15 0 * * *")
    public void monitorAccounts(){
        List<UserAccount> userAccounts =userAccountRepository.findAll();
        for(UserAccount userAccount:userAccounts){
            if(getCompanySubscription(userAccount.getCompanyId().toString()).getStatus().equals("Expired")){
                userAccount.setExpired(true);
                userAccountRepository.save(userAccount);
            }
        }

        List<AgentAccount> agentAccounts =agentAccountRepository.findAll();
        for(AgentAccount agentAccount:agentAccounts){
            if(getCompanySubscription(agentAccount.getCompanyId().toString()).getStatus().equals("Expired")){
                agentAccount.setExpired(true);
                agentAccountRepository.save(agentAccount);
            }
        }
    }
    private SubscriptionResponse getCompanySubscription(String companyId){
        return webClientBuilder.build()
                .get()
                .uri("http://subscriptions-service/api/subscription/get-subscription/"+companyId)
                .retrieve()
                .bodyToMono(SubscriptionResponse.class)
                .block();
    }
}