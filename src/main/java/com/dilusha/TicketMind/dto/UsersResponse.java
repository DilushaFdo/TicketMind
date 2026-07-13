package com.dilusha.TicketMind.dto;
import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class UsersResponse {
    private Integer id;

    private String username;

    private String firstName;

    private String lastName;

    private Boolean active;

}
