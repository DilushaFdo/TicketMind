package com.dilusha.TicketMind.repositories;

import com.dilusha.TicketMind.models.Ticket;
import com.dilusha.TicketMind.models.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TicketRepository extends JpaRepository<Ticket, Integer> {
    List<Ticket> findByIsActiveTrue();

    Optional<Ticket> findByIdAndIsActiveTrue(int ticketId);

    List<Ticket> findByCreatedByAndIsActiveTrue(User createdBy);
}
