package com.dilusha.TicketMind.services;

import com.dilusha.TicketMind.dto.UsersResponse;
import com.dilusha.TicketMind.models.User;
import com.dilusha.TicketMind.models.UserPrincipal;
import com.dilusha.TicketMind.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class UserService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    public List<UsersResponse> getUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> new UsersResponse(
                        user.getId(),
                        user.getUsername(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getActive()
                ))
                .toList();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);

        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        }
        System.out.println("User found");
        return new UserPrincipal(user);
    }


}
