package com.dilusha.TicketMind.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TicketCommentResponse {

    private Integer id;
    private String content;
    private UserSummaryResponse user;
    private LocalDateTime createdAt;
}