package com.dilusha.TicketMind.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketAssignmentRequest {

    @NotNull(message = "Assigned user ID is required")
    private Integer assignedToId;

}
