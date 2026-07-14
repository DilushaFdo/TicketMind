package com.dilusha.TicketMind.controllers;

import com.dilusha.TicketMind.dto.*;
import com.dilusha.TicketMind.models.User;
import com.dilusha.TicketMind.services.AuthService;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    AuthService authService;

    @PostMapping("/register")
    public User register(@Valid @RequestBody RegisterRequest request){
        return authService.register(request);
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(
            @RequestBody LoginRequest request,
            HttpServletResponse response){
        System.out.println("LOGIN");
        return ResponseEntity.ok(authService.verify(request,response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<RefreshResponse> refreshToken(
            @CookieValue("refreshToken") String refreshToken,
            HttpServletResponse response){
        return ResponseEntity.ok(authService.refreshToken(refreshToken, response));
    }

    @PostMapping("/logout")
    public String logout(
            @CookieValue(value = "refreshToken", required = false) String refreshToken,
            HttpServletResponse response){

            authService.logout(refreshToken, response);
        return "Successfully logged out";
    }

}
