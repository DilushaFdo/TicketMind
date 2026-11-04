package com.dilusha.TicketMind.controllers;
import com.dilusha.TicketMind.dto.UsersResponse;
import com.dilusha.TicketMind.services.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
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
    @Operation(security = @SecurityRequirement(name = "Bearer Authentication"))
    public List<UsersResponse> getUsers(){
        return userService.getUsers();
    }

}
