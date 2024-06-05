package com.servicedesk.usersservice.security;

import com.servicedesk.usersservice.model.AgentAccount;
import com.servicedesk.usersservice.repository.AgentAccountRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;


import java.io.IOException;
import java.util.Optional;

@Component
public class JwtExpiredLogoutHandler {
    private final AgentAccountRepository agentAccountRepository;

    public JwtExpiredLogoutHandler(AgentAccountRepository agentAccountRepository) {
        this.agentAccountRepository = agentAccountRepository;
    }

    public void updateStatusToOffline(String email) {
        Optional<AgentAccount> optionalAgentAccount = agentAccountRepository.findByEmail(email);
        if (optionalAgentAccount.isPresent()) {
            AgentAccount agentAccount = optionalAgentAccount.get();
            agentAccount.setStatus("Offline");
            agentAccountRepository.save(agentAccount);
        }
    }
}