package com.dilusha.TicketMind.repositories;

import com.dilusha.TicketMind.dto.RefreshResponse;
import com.dilusha.TicketMind.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    Optional<RefreshToken> findByToken(String refreshToken);
}
