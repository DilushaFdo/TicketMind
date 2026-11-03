package com.dilusha.TicketMind.controllers;

import com.dilusha.TicketMind.dto.*;
import com.dilusha.TicketMind.services.TicketService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    TicketService ticketService;

    @Operation(security = @SecurityRequirement(name = "Bearer Authentication"))
    @GetMapping
    public ResponseEntity<List<TicketsResponse>> getAllTickets(){
        return ResponseEntity.ok(
                ticketService.getAllTickets()
        );
    }

    @GetMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> getTicketById(@PathVariable int ticketId){
        return ResponseEntity.ok(
                ticketService.getTicketById(ticketId));
    }

    @Operation(security = @SecurityRequirement(name = "Bearer Authentication"))
    @PostMapping
    public ResponseEntity<TicketsResponse> createTicket(@Valid @RequestBody TicketRequest ticketRequest){
        TicketsResponse response = ticketService.addTicket(ticketRequest);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(response);
    }

    @PutMapping("/{ticketId}")
    public ResponseEntity<TicketResponse> updateTicket(
            @PathVariable int ticketId,@Valid @RequestBody TicketUpdateRequest request){
        return ResponseEntity.ok(
                ticketService.updateTicket(ticketId, request)
        );
    }

    @DeleteMapping("/{ticketId}")
    public ResponseEntity<Void> deleteTicket(@PathVariable int ticketId){
        ticketService.deleteTicket(ticketId);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{ticketId}/admin")
    public ResponseEntity<TicketResponse> adminUpdateTicket(
            @PathVariable int ticketId,
            @Valid @RequestBody TicketAdminUpdateRequest request) {

        return ResponseEntity.ok(
                ticketService.adminUpdateTicket(ticketId, request)
        );
    }

    @PatchMapping("/{ticketId}/status")
    public ResponseEntity<TicketResponse> updateTicketStatus(
            @PathVariable int ticketId,
            @Valid @RequestBody TicketStatusRequest request) {

        return ResponseEntity.ok(
                ticketService.updateTicketStatus(ticketId, request)
        );
    }

    @PatchMapping("/{ticketId}/assign")
    public ResponseEntity<TicketsResponse> assignTicket(
            @PathVariable int ticketId,
            @Valid @RequestBody TicketAssignmentRequest request) {

        return ResponseEntity.ok(
                ticketService.assignTicket(ticketId, request)
        );
    }
}
