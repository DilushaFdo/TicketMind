package com.dilusha.TicketMind.repositories;

import com.dilusha.TicketMind.models.TicketComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TicketCommentRepository
        extends JpaRepository<TicketComment, Integer> {

    List<TicketComment> findByTicketIdOrderByCreatedAtAsc(
            Integer ticketId
    );

    List<TicketComment> findByTicketIdAndTicketIsActiveTrueOrderByCreatedAtAsc(
            Integer ticketId
    );
}