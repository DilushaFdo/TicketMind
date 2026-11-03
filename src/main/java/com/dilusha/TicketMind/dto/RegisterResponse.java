package com.dilusha.TicketMind.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class RegisterResponse {

    private Integer id;
    private String username;
    private String firstName;
    private String lastName;
    private String message;
}