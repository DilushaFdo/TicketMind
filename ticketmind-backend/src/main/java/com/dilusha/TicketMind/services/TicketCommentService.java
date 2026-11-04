package com.dilusha.TicketMind.services;

import com.dilusha.TicketMind.dto.TicketCommentRequest;
import com.dilusha.TicketMind.dto.TicketCommentResponse;
import com.dilusha.TicketMind.dto.UserSummaryResponse;
import com.dilusha.TicketMind.enums.Role;
import com.dilusha.TicketMind.models.Ticket;
import com.dilusha.TicketMind.models.TicketComment;
import com.dilusha.TicketMind.repositories.TicketCommentRepository;
import com.dilusha.TicketMind.repositories.TicketRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.dilusha.TicketMind.models.User;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TicketCommentService {

    private final TicketCommentRepository commentRepository;
    private final TicketRepository ticketRepository;
    private final AuthService authService;

    @Transactional
    public TicketCommentResponse addComment(
            int ticketId,
            TicketCommentRequest request) {

        User currentUser = authService.getCurrentUser();

        Ticket ticket = ticketRepository
                .findByIdAndIsActiveTrue(ticketId)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));

        validateTicketAccess(ticket, currentUser);

        TicketComment comment = new TicketComment();
        comment.setContent(request.getContent());
        comment.setTicket(ticket);
        comment.setUser(currentUser);

        TicketComment saved = commentRepository.save(comment);

        return mapComment(saved);
    }

    @Transactional(readOnly = true)
    public List<TicketCommentResponse> getComments(int ticketId) {

        User currentUser = authService.getCurrentUser();

        Ticket ticket = ticketRepository
                .findByIdAndIsActiveTrue(ticketId)
                .orElseThrow(() ->
                        new RuntimeException("Ticket not found"));

        validateTicketAccess(ticket, currentUser);

        return commentRepository
                .findByTicketIdAndTicketIsActiveTrueOrderByCreatedAtAsc(ticketId)
                .stream()
                .map(this::mapComment)
                .toList();
    }

    private void validateTicketAccess(Ticket ticket, User user) {

        boolean isAdminOrSupport =
                user.getRole() == Role.ADMIN ||
                        user.getRole() == Role.SUPPORT_AGENT;

        boolean isOwner =
                ticket.getCreatedBy().getId().equals(user.getId());

        if (!isAdminOrSupport && !isOwner) {
            throw new AccessDeniedException(
                    "You are not allowed to access this ticket"
            );
        }
    }

    private TicketCommentResponse mapComment(TicketComment comment) {

        User user = comment.getUser();

        UserSummaryResponse userResponse =
                new UserSummaryResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getFirstName(),
                        user.getLastName()
                );

        return new TicketCommentResponse(
                comment.getId(),
                comment.getContent(),
                userResponse,
                comment.getCreatedAt()
        );
    }
}
