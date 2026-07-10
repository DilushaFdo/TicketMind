package com.dilusha.TicketMind.controllers;

import com.dilusha.TicketMind.dto.UsersResponse;
import com.dilusha.TicketMind.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public List<UsersResponse> getUsers(){
        return userService.getUsers();
    }

}
