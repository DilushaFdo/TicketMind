package com.dilusha.TicketMind.services;

import com.dilusha.TicketMind.dto.*;
import com.dilusha.TicketMind.enums.Role;
import com.dilusha.TicketMind.enums.TicketHistoryAction;
import com.dilusha.TicketMind.enums.TicketStatus;
import com.dilusha.TicketMind.models.Ticket;
import com.dilusha.TicketMind.models.User;
import com.dilusha.TicketMind.repositories.TicketRepository;
import com.dilusha.TicketMind.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private AuthService authService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TicketHistoryService ticketHistoryService;

    private UserSummaryResponse mapUser(User user) {
        if (user == null) {
            return null;
        }

        return new UserSummaryResponse(
                user.getId(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    private TicketsResponse mapTicket(Ticket ticket) {
        return new TicketsResponse(
                ticket.getId(),
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority(),
                mapUser(ticket.getCreatedBy()),
                mapUser(ticket.getAssignedTo()),
                ticket.getCreatedAt(),
                ticket.getUpdatedAt(),
                ticket.getIsActive()
        );
    }

    public List<TicketsResponse> getAllTickets() {

        User currentUser = authService.getCurrentUser();

        List<Ticket> tickets;

        if (currentUser.getRole() == Role.ADMIN ||
                currentUser.getRole() == Role.SUPPORT_AGENT) {

            tickets = ticketRepository.findByIsActiveTrue();

        } else {

            tickets = ticketRepository
                    .findByCreatedByAndIsActiveTrue(currentUser);
        }

        return tickets.stream()
                .map(this::mapTicket)
                .toList();
    }

    @Transactional
    public TicketsResponse addTicket(TicketRequest ticketRequest) {

        User user = authService.getCurrentUser();

        Ticket ticket = new Ticket();

        ticket.setTitle(ticketRequest.getTitle());
        ticket.setDescription(ticketRequest.getDescription());
        ticket.setPriority(ticketRequest.getPriority());
        ticket.setStatus(TicketStatus.OPEN);
        ticket.setCreatedBy(user);
        ticket.setIsActive(true);

        Ticket savedTicket = ticketRepository.save(ticket);

        ticketHistoryService.recordHistory(
                savedTicket,
                user,
                TicketHistoryAction.CREATED,
                null,
                "Ticket created"
        );

        return mapTicket(savedTicket);
    }

    @Transactional
    public void deleteTicket(int ticketId) {

        User currentUser = authService.getCurrentUser();

        Ticket ticket = ticketRepository
                .findByIdAndIsActiveTrue(ticketId)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));

        boolean isAdminOrSupport =
                currentUser.getRole() == Role.ADMIN ||
                        currentUser.getRole() == Role.SUPPORT_AGENT;

        boolean isOwner =
                ticket.getCreatedBy().getId()
                        .equals(currentUser.getId());

        if (!isAdminOrSupport && !isOwner) {
            throw new AccessDeniedException(
                    "You are not allowed to delete this ticket");
        }

        ticket.setIsActive(false);
    }

    public TicketResponse getTicketById(int ticketId) {
        Ticket ticket = ticketRepository.findByIdAndIsActiveTrue(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket Not Found"));

        return new TicketResponse(
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority()
        );
    }

    @Transactional
    public TicketResponse updateTicket(
            int ticketId,
            TicketUpdateRequest request) {

        User currentUser = authService.getCurrentUser();

        Ticket ticket = ticketRepository
                .findByIdAndIsActiveTrue(ticketId)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));

        boolean isAdminOrSupport =
                currentUser.getRole() == Role.ADMIN ||
                        currentUser.getRole() == Role.SUPPORT_AGENT;

        boolean isOwner =
                ticket.getCreatedBy().getId()
                        .equals(currentUser.getId());

        if (!isAdminOrSupport && !isOwner) {
            throw new org.springframework.security.access.AccessDeniedException(
                    "You are not allowed to update this ticket");
        }

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());

        return new TicketResponse(
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority()
        );
    }

    @Transactional
    public TicketResponse adminUpdateTicket(
            int ticketId,
            TicketAdminUpdateRequest request) {

        User currentUser = authService.getCurrentUser();

        if (currentUser.getRole() != Role.ADMIN &&
                currentUser.getRole() != Role.SUPPORT_AGENT) {

            throw new AccessDeniedException(
                    "You are not allowed to perform this operation");
        }

        Ticket ticket = ticketRepository
                .findByIdAndIsActiveTrue(ticketId)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());

        return new TicketResponse(
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority()
        );
    }

    @Transactional
    public TicketResponse updateTicketStatus(
            int ticketId,
            TicketStatusRequest request) {

        User currentUser = authService.getCurrentUser();

        Ticket ticket = ticketRepository
                .findByIdAndIsActiveTrue(ticketId)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));

        TicketStatus oldStatus = ticket.getStatus();

        ticket.setStatus(request.getStatus());

        ticketHistoryService.recordHistory(
                ticket,
                currentUser,
                TicketHistoryAction.STATUS_CHANGED,
                oldStatus.name(),
                request.getStatus().name()
        );

        TicketStatus currentStatus = ticket.getStatus();
        TicketStatus newStatus = request.getStatus();

        boolean isAdminOrSupport =
                currentUser.getRole() == Role.ADMIN ||
                        currentUser.getRole() == Role.SUPPORT_AGENT;

        boolean isOwner = ticket.getCreatedBy()
                .getId().equals(currentUser.getId());

        // Customer can only reopen their own resolved ticket
        if (!isAdminOrSupport) {

            if (!isOwner) {
                throw new AccessDeniedException(
                        "You cannot update this ticket's status");
            }

            if (currentStatus == TicketStatus.RESOLVED &&
                    newStatus == TicketStatus.OPEN) {

                ticket.setStatus(newStatus);

                return new TicketResponse(
                        ticket.getTitle(),
                        ticket.getDescription(),
                        ticket.getStatus(),
                        ticket.getPriority()
                );
            }

            throw new AccessDeniedException(
                    "You are not allowed to perform this status transition");
        }

        // Support agents and admins can perform operational transitions
        boolean validTransition =
                (currentStatus == TicketStatus.OPEN &&
                        newStatus == TicketStatus.IN_PROGRESS) ||

                        (currentStatus == TicketStatus.IN_PROGRESS &&
                                newStatus == TicketStatus.RESOLVED) ||

                        (currentStatus == TicketStatus.RESOLVED &&
                                newStatus == TicketStatus.CLOSED);

        if (!validTransition) {
            throw new org.springframework.web.server.ResponseStatusException(
                    org.springframework.http.HttpStatus.BAD_REQUEST,
                    "Invalid ticket status transition"
            );
        }

        ticket.setStatus(newStatus);

        return new TicketResponse(
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority()
        );
    }

    @Transactional
    public TicketsResponse assignTicket(
            int ticketId,
            TicketAssignmentRequest request) {

        User currentUser = authService.getCurrentUser();

        boolean isAdminOrSupport =
                currentUser.getRole() == Role.ADMIN ||
                        currentUser.getRole() == Role.SUPPORT_AGENT;

        if (!isAdminOrSupport) {
            throw new AccessDeniedException(
                    "You are not allowed to assign tickets"
            );
        }

        Ticket ticket = ticketRepository
                .findByIdAndIsActiveTrue(ticketId)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));

        User assignedAgent = userRepository
                .findById(request.getAssignedToId())
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        if (assignedAgent.getRole() != Role.SUPPORT_AGENT) {
            throw new IllegalArgumentException(
                    "Tickets can only be assigned to support agents"
            );
        }

        if (!Boolean.TRUE.equals(assignedAgent.getActive())) {
            throw new IllegalArgumentException(
                    "Cannot assign tickets to an inactive support agent"
            );
        }

        User oldAgent = ticket.getAssignedTo();

        ticket.setAssignedTo(assignedAgent);

        ticketHistoryService.recordHistory(
                ticket,
                currentUser,
                TicketHistoryAction.ASSIGNED,
                oldAgent != null ? oldAgent.getUsername() : null,
                assignedAgent.getUsername()
        );

        ticket.setAssignedTo(assignedAgent);

        return mapTicket(ticket);
    }
}
