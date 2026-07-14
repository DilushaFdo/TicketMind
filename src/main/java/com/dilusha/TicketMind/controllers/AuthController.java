package com.dilusha.TicketMind.controllers;

import com.dilusha.TicketMind.dto.*;
import com.dilusha.TicketMind.models.User;
import com.dilusha.TicketMind.repositories.UserRepository;
import com.dilusha.TicketMind.services.AuthService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
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
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.verify(request));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshToken(@RequestBody RefreshRequest request){
        return ResponseEntity.ok(authService.refreshToken(request));
    }

//    @PostMapping("/logout")
//    public String logout(@RequestBody LogoutRequest logoutRequest){
//
//    }

}
