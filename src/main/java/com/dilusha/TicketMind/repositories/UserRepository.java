package com.dilusha.TicketMind.repositories;

import com.dilusha.TicketMind.models.User;
import jakarta.persistence.Id;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Id> {

    User findByUsername(String username);
}
