package com.dilusha.TicketMind.repositories;

import com.dilusha.TicketMind.models.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {


}
