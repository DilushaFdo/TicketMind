package com.dilusha.TicketMind.dto;

import com.dilusha.TicketMind.enums.TicketStatus;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketStatusRequest {

    @NotNull
    private TicketStatus status;
}