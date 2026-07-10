package com.dilusha.TicketMind.controllers;

import com.dilusha.TicketMind.dto.LoginRequest;
import com.dilusha.TicketMind.dto.RegisterRequest;
import com.dilusha.TicketMind.models.User;
import com.dilusha.TicketMind.repositories.UserRepository;
import com.dilusha.TicketMind.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    AuthService authService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterRequest request){
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        return authService.register(request);
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request){
        return authService.verify(request);
    }

}
