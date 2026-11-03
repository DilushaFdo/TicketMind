package com.dilusha.TicketMind.dto;

import com.dilusha.TicketMind.enums.TicketHistoryAction;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketHistoryResponse {

    private Integer id;
    private TicketHistoryAction action;
    private String oldValue;
    private String newValue;
    private UserSummaryResponse performedBy;
    private LocalDateTime createdAt;
}
