package com.dilusha.TicketMind.services;
import com.dilusha.TicketMind.dto.*;
import com.dilusha.TicketMind.enums.Role;
import com.dilusha.TicketMind.models.RefreshToken;
import com.dilusha.TicketMind.models.User;
import com.dilusha.TicketMind.repositories.RefreshTokenRepository;
import com.dilusha.TicketMind.repositories.UserRepository;
import com.dilusha.TicketMind.util.JWTUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Date;


@Service
public class AuthService {

    @Autowired
    RefreshTokenRepository refreshTokenRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    JWTUtil jwtUtil;

    public User register(RegisterRequest request) {
        User user = new User();
        user.setUsername(request.getUsername());

        user.setPassword(
                passwordEncoder.encode(request.getPassword()));

        user.setLastName(request.getLastName());
        user.setFirstName(request.getFirstName());

        user.setRole(Role.USER);
        user.setLock(false);
        user.setActive(true);

        return userRepository.save(user);
    }

    public LoginResponse verify(
            LoginRequest request,
            HttpServletResponse response) {

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));

        User user = userRepository.findByUsername(request.getUsername());

        String accessToken = jwtUtil.generateAccessToken(user);
        String refreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        Date expiryDate = jwtUtil.extractExpirationDate(refreshToken);

        long secondsUntilExpiry =
                (expiryDate.getTime() - System.currentTimeMillis()) / 1000;

        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", refreshToken)
                .httpOnly(true)
                .secure(false)
                .path("/auth")
                .maxAge(Duration.ofSeconds(secondsUntilExpiry))
                .sameSite("Strict")
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );

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

            return new LoginResponse(accessToken);
    }

    @Transactional
    public RefreshResponse refreshToken(
            String refreshToken,
            HttpServletResponse response) {

        RefreshToken storedToken = refreshTokenRepository
                .findByToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if(storedToken.getRevoked()){
            throw new RuntimeException("Refresh token revoked");
        }

        String type = jwtUtil.extractTokenType(refreshToken);

        if(jwtUtil.isTokenExpired(refreshToken)){
            throw new RuntimeException("Refresh token Expired");
        }

        if(!"refresh".equals(type)){
            throw new RuntimeException("Invalid token type");
        }

        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        User user = storedToken.getUser();

        String newAccessToken = jwtUtil.generateAccessToken(user);
        String newRefreshToken = jwtUtil.generateRefreshToken(user.getUsername());

        RefreshToken newStoredRefreshToken = new RefreshToken();
        newStoredRefreshToken.setToken(newRefreshToken);
        newStoredRefreshToken.setRevoked(false);
        newStoredRefreshToken.setUser(storedToken.getUser());

        refreshTokenRepository.save(newStoredRefreshToken);

        ResponseCookie cookie = ResponseCookie
                .from("refreshToken", newRefreshToken)
                .path("/")
                .httpOnly(true)
                .secure(false)
                .maxAge(Duration.ofDays(7))
                .sameSite("Strict")
                .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                cookie.toString()
        );

        return new RefreshResponse(newAccessToken);

    }

    public void logout(String refreshToken, HttpServletResponse response) {

        if(refreshToken != null){

            RefreshToken storedToken =
                    refreshTokenRepository.findByToken(refreshToken)
                            .orElse(null);

            if(storedToken != null){
                storedToken.setRevoked(true);
                refreshTokenRepository.save(storedToken);
            }
        }

        ResponseCookie deleteCookie=
                ResponseCookie.from("refreshToken","")
                        .secure(false)
                        .path("/auth")
                        .maxAge(0)
                        .httpOnly(true)
                        .sameSite("STRICT")
                        .build();

        response.addHeader(
                HttpHeaders.SET_COOKIE,
                deleteCookie.toString()
        );

    }
}
