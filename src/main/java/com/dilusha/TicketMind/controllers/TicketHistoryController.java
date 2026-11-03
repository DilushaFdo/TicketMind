package com.dilusha.TicketMind.controllers;

import com.dilusha.TicketMind.dto.TicketHistoryResponse;
import com.dilusha.TicketMind.enums.Role;
import com.dilusha.TicketMind.models.Ticket;
import com.dilusha.TicketMind.repositories.TicketRepository;
import com.dilusha.TicketMind.services.AuthService;
import com.dilusha.TicketMind.services.TicketHistoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.dilusha.TicketMind.models.User;

import java.util.List;

@RestController
@RequestMapping("/tickets/{ticketId}/history")
@RequiredArgsConstructor
public class TicketHistoryController {

    private final TicketHistoryService historyService;
    private final TicketRepository ticketRepository;
    private final AuthService authService;

    @GetMapping
    public ResponseEntity<List<TicketHistoryResponse>> getHistory(
            @PathVariable int ticketId) {

        User currentUser = authService.getCurrentUser();

        Ticket ticket = ticketRepository
                .findByIdAndIsActiveTrue(ticketId)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));

        boolean isAdminOrSupport =
                currentUser.getRole() == Role.ADMIN ||
                        currentUser.getRole() == Role.SUPPORT_AGENT;

        boolean isOwner =
                ticket.getCreatedBy().getId().equals(currentUser.getId());

        if (!isAdminOrSupport && !isOwner) {
            throw new AccessDeniedException(
                    "You are not allowed to view this history"
            );
        }

        return ResponseEntity.ok(
                historyService.getTicketHistory(ticketId)
        );
    }
}
