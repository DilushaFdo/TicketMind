package com.dilusha.TicketMind.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class LoginRequest {
    @NotEmpty(message = "Enter the correct username")
    private String username;

    @NotEmpty(message = "Enter the correct password")
    private String password;
}
