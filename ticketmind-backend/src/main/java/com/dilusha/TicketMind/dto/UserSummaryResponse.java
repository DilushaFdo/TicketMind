package com.dilusha.TicketMind.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserSummaryResponse {
    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
}