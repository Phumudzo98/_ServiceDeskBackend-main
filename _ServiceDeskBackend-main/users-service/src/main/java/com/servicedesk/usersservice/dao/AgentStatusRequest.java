package com.servicedesk.usersservice.dao;

public class AgentStatusRequest {

    private String email;
    private String statusAgent;

    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getStatusAgent() {
        return statusAgent;
    }

    public void setStatusAgent(String statusAgent) {
        this.statusAgent = statusAgent;
    }
}
