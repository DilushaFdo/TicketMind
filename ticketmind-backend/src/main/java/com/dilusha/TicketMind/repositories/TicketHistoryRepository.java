package com.dilusha.TicketMind.repositories;

import com.dilusha.TicketMind.models.TicketHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketHistoryRepository
        extends JpaRepository<TicketHistory, Integer> {

    List<TicketHistory> findByTicketIdOrderByCreatedAtDesc(
            Integer ticketId
    );
}