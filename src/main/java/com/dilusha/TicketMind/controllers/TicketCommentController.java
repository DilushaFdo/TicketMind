package com.dilusha.TicketMind.controllers;

import com.dilusha.TicketMind.dto.TicketCommentRequest;
import com.dilusha.TicketMind.dto.TicketCommentResponse;
import com.dilusha.TicketMind.services.TicketCommentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tickets/{ticketId}/comments")
@RequiredArgsConstructor
public class TicketCommentController {

    private final TicketCommentService commentService;

    @PostMapping
    public ResponseEntity<TicketCommentResponse> addComment(
            @PathVariable int ticketId,
            @Valid @RequestBody TicketCommentRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(commentService.addComment(ticketId, request));
    }

    @GetMapping
    public ResponseEntity<List<TicketCommentResponse>> getComments(
            @PathVariable int ticketId) {

        return ResponseEntity.ok(
                commentService.getComments(ticketId)
        );
    }
}
