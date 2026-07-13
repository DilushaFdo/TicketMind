package com.dilusha.TicketMind.services;

import com.dilusha.TicketMind.dto.LoginRequest;
import com.dilusha.TicketMind.dto.LoginResponse;
import com.dilusha.TicketMind.dto.RegisterRequest;
import com.dilusha.TicketMind.enums.Role;
import com.dilusha.TicketMind.models.RefreshToken;
import com.dilusha.TicketMind.models.User;
import com.dilusha.TicketMind.repositories.RefreshTokenRepository;
import com.dilusha.TicketMind.repositories.UserRepository;
import com.dilusha.TicketMind.util.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
public class AuthService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JWTUtil jwtUtil;

    public User register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(request.getPassword());
        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());

        user.setRole(Role.ROLE_USER);
        user.setLock(false);
        user.setActive(true);


        return userRepository.save(user);
    }

    public ResponseEntity<LoginResponse> verify(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));

        if(authentication.isAuthenticated()){
            User user = userRepository.findByUsername(request.getUsername());

            String accessToken = jwtUtil.generateAccessToken(user.getUsername());
            String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

//            Date expiryDate = jwtUtil.extractExpirationDate(refreshToken);

            RefreshToken newRefreshToken = new RefreshToken();

            newRefreshToken.setToken(refreshToken);
            newRefreshToken.setUser(user);
            newRefreshToken.setRevoked(false);
//            newRefreshToken.setExpiryDate(
//                    expiryDate.toInstant()
//                            .atZone(ZoneId.systemDefault())
//                            .toLocalDateTime()
//            );

            refreshTokenRepository.save(newRefreshToken);

            return ResponseEntity.ok(new LoginResponse(accessToken, refreshToken));
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();

    }
}
