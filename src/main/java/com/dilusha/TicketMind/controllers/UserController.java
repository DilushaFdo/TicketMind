package com.dilusha.TicketMind.controllers;
import com.dilusha.TicketMind.dto.UsersResponse;
import com.dilusha.TicketMind.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/users")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UsersResponse> getUsers(){
        return userService.getUsers();
    }

}
