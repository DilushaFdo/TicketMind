package com.dilusha.TicketMind.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class TicketCommentRequest {

    @NotBlank(message = "Comment cannot be empty")
    @Size(max = 3000, message = "Comment cannot exceed 3000 characters")
    private String content;
}
