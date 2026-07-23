package com.dilusha.TicketMind.services;

import com.dilusha.TicketMind.dto.TicketRequest;
import com.dilusha.TicketMind.dto.TicketResponse;
import com.dilusha.TicketMind.dto.TicketUpdateRequest;
import com.dilusha.TicketMind.dto.TicketsResponse;
import com.dilusha.TicketMind.enums.TicketStatus;
import com.dilusha.TicketMind.models.Ticket;
import com.dilusha.TicketMind.models.User;
import com.dilusha.TicketMind.repositories.TicketRepository;
import com.dilusha.TicketMind.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TicketService {

    @Autowired
    TicketRepository ticketRepository;

    @Autowired
    AuthService authService;

    @Autowired
    UserRepository userRepository;

    public List<TicketsResponse> getAllTickets() {
        return ticketRepository.findByIsActiveTrue()
                .stream()
                .map(ticket -> new TicketsResponse(
                        ticket.getId(),
                        ticket.getTitle(),
                        ticket.getDescription(),
                        ticket.getStatus(),
                        ticket.getPriority(),
                        ticket.getCreatedBy(),
                        ticket.getAssignedTo(),
                        ticket.getCreatedAt(),
                        ticket.getUpdatedAt(),
                        ticket.getIsActive()
                ))
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

        return new TicketsResponse(
                savedTicket.getId(),
                savedTicket.getTitle(),
                savedTicket.getDescription(),
                savedTicket.getStatus(),
                savedTicket.getPriority(),
                savedTicket.getCreatedBy(),
                savedTicket.getAssignedTo(),
                savedTicket.getCreatedAt(),
                savedTicket.getUpdatedAt(),
                savedTicket.getIsActive()
        );
    }

    @Transactional
    public void deleteTicket(int ticketId) {
        Ticket ticket = ticketRepository.findById(ticketId)
                .orElseThrow(() -> new RuntimeException("Ticket Not found"));

        ticket.setIsActive(false);
    }

    public TicketResponse getTicketById(int ticketId) {
        Ticket ticket = ticketRepository.findByIdAndIsActiveTrue(ticketId)
                .orElseThrow(()-> new RuntimeException("Ticket Not Found"));

        return new TicketResponse(
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority()
        );
    }

    @Transactional
    public TicketResponse updateTicket(int ticketId, TicketUpdateRequest request) {
        Ticket ticket = ticketRepository.findByIdAndIsActiveTrue(ticketId)
                .orElseThrow(()-> new RuntimeException("Ticket Not Found"));

        ticket.setTitle(request.getTitle());
        ticket.setDescription(request.getDescription());
        ticket.setPriority(request.getPriority());

        ticketRepository.save(ticket);

        return new TicketResponse(
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getStatus(),
                ticket.getPriority()
        );
    }
}
