package com.dilusha.TicketMind.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketUpdateRequest {

    @NotBlank
    private String title;

    @NotBlank
    private String description;

}
