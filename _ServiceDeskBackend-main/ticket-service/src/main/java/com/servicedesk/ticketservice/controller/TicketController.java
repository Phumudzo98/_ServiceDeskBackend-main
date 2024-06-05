package com.servicedesk.ticketservice.controller;

import com.servicedesk.ticketservice.dao.*;
import com.servicedesk.ticketservice.repository.TicketRepository;
import com.servicedesk.ticketservice.service.TicketService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/ticket")
@RequiredArgsConstructor
public class TicketController {
    private final TicketService ticketService;
 private  final TicketRepository ticketRepository;
    @PostMapping(value = "/request-service/{companyId}")
    public CompletableFuture<ResponseEntity<Response>> requestService(@PathVariable("companyId") String companyId,
                                                                      @RequestBody TicketRequest ticketRequest) {
        return ticketService.createTicket(ticketRequest, companyId)
                .thenApply(message -> ResponseEntity.ok(new Response(message)))
                .exceptionally(e -> {
                    e.printStackTrace();
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }


    @PutMapping(value = "/update-ticket",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> updateTicket(@RequestBody TicketUpdate ticketUpdate){

        System.out.println(ticketUpdate);

        if(ticketService.UpdateTicket(ticketUpdate))
            return ResponseEntity.ok(new Response("Ticket updated successful"));

        return ResponseEntity.badRequest().body("Error ! Failed to update");
    }

    @GetMapping(value = "/get-user-tickets/{customerUserId}")
    public List<TicketResponse> getUserTickets(@PathVariable("customerUserId") String customerUserId){
        return  ticketService.userTickets(customerUserId);
    }

    @GetMapping(value = "/get-agent-tickets/{customerAgentId}")
    public List<TicketResponse> getAgentTickets(@PathVariable("customerAgentId") String customerAgentId){
        return  ticketService.agentTickets(customerAgentId);
    }

    @GetMapping(value = "/get-ticket/{ticketId}")
    public TicketRespond getTicket(@PathVariable("ticketId") String ticketId){
        return ticketService.getTicket(ticketId);
    }

    @GetMapping(value = "/get-tickets/{companyId}")
    public List<TicketRespond> getTickets(@PathVariable("companyId") String companyId){
        return ticketService.ticketRespondList(companyId);
    }

    @GetMapping(value = "/get-resolved-tickets/{companyId}")
    public List<TicketRespond> getResolvedTickets(@PathVariable("companyId") String companyId){
        return ticketService.resolvedTickets(UUID.fromString(companyId));
    }
    @GetMapping("/tickets/resolved")
    public ResponseEntity<Integer> getResolvedTicketsEachMonth(
            @RequestParam UUID companyId,
            @RequestParam Date updatedAt) {

        int tickets = ticketService.getResolvedTicketsEachMonth( companyId, updatedAt);
        return ResponseEntity.ok(tickets);
    }
    @GetMapping("/tickets/escalate")
    public ResponseEntity<Integer> getEscalatedTicketsEachMonth(
            @RequestParam UUID companyId,
            @RequestParam Date updatedAt) {

        int tickets = ticketService.getEscalatedTicketsEachMonth( companyId, updatedAt);
        return ResponseEntity.ok(tickets);
    }
    @GetMapping("/tickets/open")
    public ResponseEntity<Integer> getOpenTicketsEachMonth(
            @RequestParam UUID companyId,
            @RequestParam Date createdAt) {

        int tickets = ticketService.getOpenTicketsEachMonth( companyId,createdAt);
        return ResponseEntity.ok(tickets);
    }
    @GetMapping("/tickets/progress")
    public ResponseEntity<Integer> getProgressTicketsEachMonth(
            @RequestParam UUID companyId,
            @RequestParam Date updatedAt) {

        int tickets = ticketService.getProgressTicketsEachMonth( companyId, updatedAt);
        return ResponseEntity.ok(tickets);
    }
    @GetMapping(value = "/get-totalResolved/{companyId}",produces = MediaType.APPLICATION_JSON_VALUE)
    public ticket getUsers(@PathVariable("companyId") String companyId){
        return ticketService.ticketData(UUID.fromString(companyId));
    }

    @GetMapping(value = "/admin-tickets/{companyId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<AdminTicketRespond>> getAdminTickets(@PathVariable("companyId") String companyId){
        List<AdminTicketRespond> ticketRespondList = ticketService.adminTickets(companyId);
        return ResponseEntity.ok(ticketRespondList);
    }

    @PutMapping(value = "/reassign-ticket/{ticketId}/{agentId}")
    public CompletableFuture<ResponseEntity<Response>> reassignTicket(@PathVariable("ticketId") String ticketId, @PathVariable("agentId") String agentId) {
        return ticketService.reassignTicketAgent(ticketId, agentId)
                .thenApply(result -> {
                    if (result) {
                        return ResponseEntity.ok(new Response("Ticket reassigned successfully"));
                    } else {
                        return ResponseEntity.badRequest().body(new Response("Failed to reassign the ticket"));
                    }
                })
                .exceptionally(ex -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                        .body(new Response("An error occurred while reassigning the ticket")));
    }

}