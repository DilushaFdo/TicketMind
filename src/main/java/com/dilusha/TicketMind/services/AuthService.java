package com.dilusha.TicketMind.services;
import com.dilusha.TicketMind.dto.*;
import com.dilusha.TicketMind.enums.Role;
import com.dilusha.TicketMind.models.RefreshToken;
import com.dilusha.TicketMind.models.User;
import com.dilusha.TicketMind.repositories.RefreshTokenRepository;
import com.dilusha.TicketMind.repositories.UserRepository;
import com.dilusha.TicketMind.util.JWTUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


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

        user.setRole(Role.ROLE_USER);
        user.setLock(false);
        user.setActive(true);

        return userRepository.save(user);
    }

    public LoginResponse verify(LoginRequest request) {

        Authentication authentication =
                authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(),request.getPassword()));

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

            return new LoginResponse(accessToken, refreshToken);

    }

    @Transactional
    public RefreshResponse refreshToken(RefreshRequest request) {
        RefreshToken storedToken = refreshTokenRepository
                .findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RuntimeException("Refresh token not found"));

        if(storedToken.getRevoked()){
            throw new RuntimeException("Refresh token revoked");
        }
        String refreshToken = storedToken.getToken();
        String type = jwtUtil.extractTokenType(refreshToken);
        String username = jwtUtil.extractUsername(refreshToken);

        if(jwtUtil.isTokenExpired(refreshToken)){
            throw new RuntimeException("Refresh token Expired");
        }

        if(!"refresh".equals(type)){
            throw new RuntimeException("Invalid token type");
        }

        storedToken.setRevoked(true);
        refreshTokenRepository.save(storedToken);

        String newAccessToken = jwtUtil.generateAccessToken(username);
        String newRefreshToken = jwtUtil.generateRefreshToken(username);

        RefreshToken newStoredRefreshToken = new RefreshToken();
        newStoredRefreshToken.setToken(newRefreshToken);
        newStoredRefreshToken.setRevoked(false);
        newStoredRefreshToken.setUser(storedToken.getUser());

        refreshTokenRepository.save(newStoredRefreshToken);

        return new RefreshResponse(newAccessToken, newRefreshToken);

    }
}
