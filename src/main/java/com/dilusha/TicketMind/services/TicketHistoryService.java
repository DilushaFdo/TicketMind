package com.dilusha.TicketMind.services;

import com.dilusha.TicketMind.dto.TicketHistoryResponse;
import com.dilusha.TicketMind.dto.UserSummaryResponse;
import com.dilusha.TicketMind.enums.TicketHistoryAction;
import com.dilusha.TicketMind.models.Ticket;
import com.dilusha.TicketMind.models.TicketHistory;
import com.dilusha.TicketMind.repositories.TicketHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.dilusha.TicketMind.models.User;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketHistoryService {

    private final TicketHistoryRepository historyRepository;

    public void recordHistory(
            Ticket ticket,
            User performedBy,
            TicketHistoryAction action,
            String oldValue,
            String newValue) {

        TicketHistory history = new TicketHistory();

        history.setTicket(ticket);
        history.setPerformedBy(performedBy);
        history.setAction(action);
        history.setOldValue(oldValue);
        history.setNewValue(newValue);

        historyRepository.save(history);
    }

    @Transactional(readOnly = true)
    public List<TicketHistoryResponse> getTicketHistory(int ticketId) {

        return historyRepository
                .findByTicketIdOrderByCreatedAtDesc(ticketId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private TicketHistoryResponse mapToResponse(TicketHistory history) {

        User user = history.getPerformedBy();

        UserSummaryResponse userResponse =
                new UserSummaryResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getFirstName(),
                        user.getLastName()
                );

        return new TicketHistoryResponse(
                history.getId(),
                history.getAction(),
                history.getOldValue(),
                history.getNewValue(),
                userResponse,
                history.getCreatedAt()
        );
    }
}
