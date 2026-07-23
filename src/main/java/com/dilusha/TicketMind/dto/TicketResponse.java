package com.dilusha.TicketMind.dto;

import com.dilusha.TicketMind.enums.TicketPriority;
import com.dilusha.TicketMind.enums.TicketStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@Data
@NoArgsConstructor
public class TicketResponse {

    private String title;

    private String description;

    private TicketStatus status;

    private TicketPriority priority;


}
