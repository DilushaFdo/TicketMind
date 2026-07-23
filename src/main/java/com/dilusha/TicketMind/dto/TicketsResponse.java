package com.dilusha.TicketMind.dto;

import com.dilusha.TicketMind.enums.TicketPriority;
import com.dilusha.TicketMind.enums.TicketStatus;
import com.dilusha.TicketMind.models.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketsResponse {

    private Integer id;

    private String title;

    private String description;

    private TicketStatus status;

    private TicketPriority priority;

    private User createdBy;

    private User assignedTo;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean isActive;


}
