package com.servicedesk.ticketservice.service;

import com.servicedesk.ticketservice.config.TicketWebSocketHandler;
import com.servicedesk.ticketservice.dao.*;
import com.servicedesk.ticketservice.event.EscalateEvent;
import com.servicedesk.ticketservice.model.EscalatedTicket;
import com.servicedesk.ticketservice.model.InProgressTicket;
import com.servicedesk.ticketservice.model.Ticket;
import com.servicedesk.ticketservice.model.TicketResolution;
import com.servicedesk.ticketservice.repository.TicketRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
public class TicketService {
    private final WebClient.Builder webClientBuilder;
    private final TicketRepository ticketRepository;
    private final TicketWebSocketHandler webSocketHandler;
    private final SendEmailService sendEmailService;

    public TicketService(WebClient.Builder webClientBuilder, TicketRepository ticketRepository,
                         TicketWebSocketHandler webSocketHandler, SendEmailService sendEmailService) {
        this.webClientBuilder = webClientBuilder;
        this.ticketRepository = ticketRepository;
        this.webSocketHandler = webSocketHandler;
        this.sendEmailService = sendEmailService;
    }

    @Transactional
    public CompletableFuture<String> createTicket(TicketRequest ticketRequest, String companyId) {
        CompletableFuture<AgentResponse> agentResponseFuture = CompletableFuture.supplyAsync(() -> availableAgent(companyId));

        return agentResponseFuture.thenCompose(agentResponse -> {
            if(agentResponse==null)
                return CompletableFuture.completedFuture("Something went wrong, try again later..");

            List<Ticket> tickets = ticketRepository.findByByCustomerUserId(UUID.fromString(ticketRequest.getCustomerUserId()));

            if (tickets.size() > 0) {
                //boolean similarTicketFound = false;
                for (Ticket ticket : tickets) {
                    if (ticket.getStatus().equals("Open")) {
                        if (similarity(ticket.getDescription(), ticketRequest.getDescription()) > 0.5) {
                            return CompletableFuture.completedFuture("You have already logged a request similar to this and " +
                                    "the responsible agent is yet to attend to it");
                        }
                    }
                }
            }
            return storeTicket(ticketRequest, companyId, agentResponse);
        }).exceptionally(e->{
            e.printStackTrace();
            return "An error occurred while creating the ticket.";
        });
    }

    private CompletableFuture<String> storeTicket(TicketRequest ticketRequest, String companyId, AgentResponse agentResponse) {
        return CompletableFuture.supplyAsync(() -> {
            // Existing code to store the ticket
            int priorityLevel=determinePriorityLevel(ticketRequest.getDescription());
            Ticket ticket=Ticket.builder()
                    .companyId(UUID.fromString(companyId))
                    .customerUserId(UUID.fromString(ticketRequest.getCustomerUserId()))
                    .category(ticketRequest.getCategory())
                    .description(ticketRequest.getDescription())
                    .customerAgentId(agentResponse.getAccountId())
                    .createdAt(new Date())
                    .status("Open")
                    .priority(priority(priorityLevel))
                    .build();
            ticketRepository.save(ticket);
            webSocketHandler.sendTicketNotification(ticket.getCustomerAgentId().toString(),ticket);

            // Send email asynchronously
            CompletableFuture<Boolean> emailTask = CompletableFuture.supplyAsync(() -> {
                String content = "Hi " + agentResponse.getFullName() + ",\n\n" +
                        "Ticket has been assigned to you.\n\n" +
                        "Thanks,\n" +
                        "The Service Desk team";
                return sendEmailService.sendEmail(agentResponse.getEmail(), "Ticket Assigned", content);
            });

            // Return the message when the email task completes
            try {
                boolean isSent = emailTask.get();
                System.out.println(isSent);
                return "You successfully logged a service request, Agent - " +
                        agentResponse.getFullName() + " will attend to it soon";
            } catch (InterruptedException | ExecutionException e) {
                // Handle exceptions if necessary
                e.printStackTrace();
                return "An error occurred while sending the email";
            }
        });
    }

    private int determinePriorityLevel(String description){
        int priorityLevel=0;
        if(description.toLowerCase().contains("can't work") || description.toLowerCase().contains("cannot work")){
           priorityLevel=4;
        } else if (description.toLowerCase().contains("not working") || description.toLowerCase().contains("no signal")) {
            priorityLevel=3;
        } else if (description.toLowerCase().contains("urgent") || description.toLowerCase().contains("emergency")) {
            priorityLevel=5;
        } else if (description.toLowerCase().contains("wifi")) {
            priorityLevel=1;
        }else {
            priorityLevel=2;
        }
        return priorityLevel;
    }

    private String priority(int priorityLevel){
        String priority="";

        if(priorityLevel<3)
            priority="Low";
        if(priorityLevel>=3 && priorityLevel<=4)
            priority="Medium";
        if(priorityLevel==5)
            priority="High";

        return priority;
    }

    private static double similarity(String s1, String s2) {
        int len1 = s1.length();
        int len2 = s2.length();
        int[][] dp = new int[len1 + 1][len2 + 1];

        for (int i = 0; i <= len1; i++) {dp[i][0] = i;}

        for (int j = 0; j <= len2; j++) {dp[0][j] = j;}

        for (int i = 1; i <= len1; i++) {
            for (int j = 1; j <= len2; j++) {
                int cost = (s1.charAt(i - 1) == s2.charAt(j - 1)) ? 0 : 1;
                dp[i][j] = Math.min(Math.min(dp[i - 1][j] + 1, dp[i][j - 1] + 1), dp[i - 1][j - 1] + cost);
            }
        }

        int maxLen = Math.max(len1, len2);
        return ((double) (maxLen - dp[len1][len2])) / maxLen;
    }

    @CircuitBreaker(name = "user-service",fallbackMethod = "getAgentsFallback")
    private List<AgentResponse> getAgents(String companyId){
        try{
            return List.of(Objects.requireNonNull(webClientBuilder.build().get()
                    .uri("http://users-service/api/users/get-agents/" + companyId)
                    .retrieve()
                    .bodyToMono(AgentResponse[].class)
                    .block()));
        }catch (WebClientResponseException exception){
            exception.printStackTrace();
            return new ArrayList<>();
        }

    }

    private AgentResponse availableAgent(String companyId){

        int openTickets=0;
        int minOpenTickets = Integer.MAX_VALUE;

        AgentResponse availableAgentResponse=null;

        List<AgentResponse> agentResponseList=getAgents(companyId);

        System.out.println(agentResponseList);

        if(agentResponseList.size()==0)
            return null;

        for(AgentResponse agentResponse:agentResponseList){
            if(agentResponse.isOnLeave())
                continue;

            List<Ticket> ticketList = ticketRepository.findByCustomerAgentId(agentResponse.getAccountId());
            for(Ticket ticket:ticketList){
                if(ticket.getStatus().equals("Open"))
                    openTickets+=1;
            }
            if(openTickets< minOpenTickets){
                minOpenTickets=openTickets;
                availableAgentResponse=agentResponse;
            }
        }
        return availableAgentResponse;
    }

    public boolean UpdateTicket(TicketUpdate ticketUpdate){

        Ticket ticket =null;
        if(ticketRepository.findById(UUID.fromString(ticketUpdate.getTicketId())).isPresent()) {
            ticket = ticketRepository.findById(UUID.fromString(ticketUpdate.getTicketId())).get();
        }else {
            return false;
        }

        ticket.setStatus(ticketUpdate.getStatus());

        if(ticketUpdate.getStatus().equals("Escalate") && ticketUpdate.getEscalatedToAgentId() !=null){

            AgentResponse escalatedTo =this.getAgent(ticketUpdate.getEscalatedToAgentId());

            AgentResponse escalatingAgent = this.getAgent(ticket.getCustomerAgentId().toString());

            if(escalatingAgent == null)
                return false;

            assert escalatedTo != null;
            EscalateEvent event = EscalateEvent.builder()
                    .escalatedByFullName(escalatingAgent.getFullName())
                    .escalatedToEmail(escalatedTo.getEmail())
                    .escalatedToFullName(escalatedTo.getFullName())
                    .reason(ticketUpdate.getUpdateMessage())
                    .build();

            String content = "Hi " + event.getEscalatedToFullName() + ",\n\n" +
                    event.getEscalatedByFullName()+" has escalated ticket to you.\n\n" +
                    "Reason: "+event.getReason()+"\n\n" +
                    "Thanks,\n" +
                    "The Service Desk team";

            boolean isSent = sendEmailService.sendEmail(event.getEscalatedToEmail(), "Escalated Ticket",content);

            EscalatedTicket escalatedTicket = EscalatedTicket.builder()
                    .reason(ticketUpdate.getUpdateMessage())
                    .escalatedTo(UUID.fromString(ticketUpdate.getEscalatedToAgentId()))
                     .build();
            ticket.setUpdateAt(new Date());
            ticket.setEscalatedTicket(escalatedTicket);
            escalatedTicket.setTicket(ticket);


        }

        if(ticketUpdate.getStatus().equals("Closed") && ticketUpdate.getUpdateMessage() != null){
            TicketResolution resolution=TicketResolution.builder().resolution(ticketUpdate.getUpdateMessage()).build();
            ticket.setTicketResolution(resolution);
            resolution.setTicket(ticket);
            ticket.setUpdateAt(new Date());

        }
        else
        if(ticketUpdate.getStatus().equals("In progress") && ticketUpdate.getUpdateMessage() != null){
            InProgressTicket progress=InProgressTicket.builder().reason(ticketUpdate.getUpdateMessage()).build();
         ticket.setInProgressTicket(progress);
         progress.setTicket(ticket);
         ticket.setUpdateAt(new Date());
        }

        ticketRepository.save(ticket);
        return true;
    }

    private UserResponse getUser(String agentId){

        try{
            return webClientBuilder.build().get()
                    .uri("http://users-service/api/users/get-user/" + agentId)
                    .retrieve()
                    .bodyToMono(UserResponse.class)
                    .block();
        }catch (WebClientResponseException exception){
            System.out.println(exception.getMessage());
            return null;
        }
    }

    private AgentResponse getAgent(String agentId){

        try{
            return webClientBuilder.build().get()
                    .uri("http://users-service/api/users/get-agent/" + agentId)
                    .retrieve()
                    .bodyToMono(AgentResponse.class)
                    .block();
        }catch (WebClientResponseException exception){
            System.out.println(exception.getMessage());
            return null;
        }
    }

    public List<TicketResponse> userTickets(String customerUserId){
        List<Ticket> tickets = ticketRepository.findByByCustomerUserId(UUID.fromString(customerUserId));
        return tickets.stream().map(this::mapToAgentUserTickets).toList();
    }

    public List<TicketResponse> agentTickets(String customerAgentId){
        List<Ticket> tickets = ticketRepository.findByCustomerAgentId(UUID.fromString(customerAgentId));
        return tickets.stream().map(this::mapToAgentUserTickets).toList();
    }

    private  TicketResponse mapToAgentUserTickets(Ticket ticket){
        return TicketResponse.builder()
                .ticketId(ticket.getTicketId().toString())
                .category(ticket.getCategory())
                .createdAt(ticket.getCreatedAt())
                .description(ticket.getDescription())
                .priority(ticket.getPriority())
                .status(ticket.getStatus())
                .build();
    }

    //get ticket using its id
    public TicketRespond getTicket(String ticketID){

        if(ticketRepository.findById(UUID.fromString(ticketID)).isPresent()){
            Ticket ticket= ticketRepository.findById(UUID.fromString(ticketID)).get();

            return TicketRespond.builder()
                    .category(ticket.getCategory())
                    .description(ticket.getDescription())
                    .status(ticket.getStatus())
                    .createdAt(ticket.getCreatedAt())
                    .priority(ticket.getPriority())
                    .assignedTo(ticket.getCustomerAgentId().toString())
                    .updateAt(ticket.getUpdateAt())
                    .build();
        }
        return null;
    }

    public List<TicketRespond> ticketRespondList(String companyId){
        List<Ticket> tickets = ticketRepository.findByCompanyId(UUID.fromString(companyId));
        return tickets.stream().map(this::mapToTickets).toList();
    }

    public List<TicketRespond> resolvedTickets(UUID companyId){
        String status="Closed";
        List<Ticket> tickets =ticketRepository.findResolvedTickets(status,companyId);
        return  tickets.stream().map(this::mapToTickets).toList();
    }
   public  ticket ticketData (UUID companyId){
        String status ="Closed";
        List<Ticket> tickets=ticketRepository.findResolvedTickets(status,companyId);

       return ticket.builder()
               .Resolved(tickets.size()).build();
   }
    private  TicketRespond mapToTickets(Ticket ticket){
        return TicketRespond.builder()
                .ticketId(ticket.getTicketId())
                .category(ticket.getCategory())
                .createdAt(ticket.getCreatedAt())
                .description(ticket.getDescription())
                .priority(ticket.getPriority())
                .status(ticket.getStatus())
                .updateAt(ticket.getUpdateAt())
                .assignedTo(ticket.getCustomerAgentId().toString())
                .build();
    }

    public List<AdminTicketRespond> adminTickets(String companyId){
        List<Ticket> tickets = ticketRepository.findByCompanyId(UUID.fromString(companyId));
        return tickets.stream().map(this::mapToAdminTicketRespond).toList();
    }

    private AdminTicketRespond mapToAdminTicketRespond(Ticket ticket){
        return AdminTicketRespond.builder()
                .ticketId(ticket.getTicketId().toString())
                .loggedBy(Objects.requireNonNull(getUser(ticket.getCustomerUserId().toString())).getFullName())
                .createdAt(ticket.getCreatedAt())
                .assignedTo(Objects.requireNonNull(getAgent(ticket.getCustomerAgentId().toString())).getFullName())
                .status(ticket.getStatus())
                .category(ticket.getCategory())
                .priority(ticket.getPriority()).build();
    }
    public int getResolvedTicketsEachMonth( UUID companyId, java.sql.Date updatedAt) {
        String status="Closed";
        return ticketRepository.findTicketsEachMonth(status, companyId.toString(), updatedAt);
    }

    public int getEscalatedTicketsEachMonth( UUID companyId, java.sql.Date updatedAt) {
        String status="Escalate";
        return ticketRepository.findTicketsEachMonth(status, companyId.toString(), updatedAt);
    }

    public int getOpenTicketsEachMonth( UUID companyId, java.sql.Date createdAt) {
        String status="Open";
        return ticketRepository.findTicketsOPenEachMonth(status, companyId.toString(), createdAt);
    }

    public int getProgressTicketsEachMonth( UUID companyId, java.sql.Date updatedAt) {
        String status="In progress";
        return ticketRepository.findTicketsEachMonth(status, companyId.toString(), updatedAt);
    }

    public CompletableFuture<Boolean> reassignTicketAgent(String ticketId, String agentId) {
        return CompletableFuture.supplyAsync(() -> {
            AgentResponse agentResponse = getAgent(agentId);
            if (agentResponse == null) {
                return false;
            }

            String content = "Hi " + agentResponse.getFullName() + ",\n\n" +
                    "Ticket has been assigned to you.\n\n" +
                    "Thanks,\n" +
                    "The Service Desk team";

            if (ticketRepository.findById(UUID.fromString(ticketId)).isPresent()) {
                Ticket ticket = ticketRepository.findById(UUID.fromString(ticketId)).get();
                ticket.setCustomerAgentId(UUID.fromString(agentId));
                ticketRepository.save(ticket);
                boolean isSent = sendEmailService.sendEmail(agentResponse.getEmail(), "Ticket Assigned", content);
                return true;
            }
            return false;
        }).exceptionally(e -> {
            e.printStackTrace();
            return false;
        });
    }
}