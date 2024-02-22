package dev.grebles.norah.services;

import dev.grebles.norah.dto.request.UserDto;
import dev.grebles.norah.entities.User;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface UserService {

    UserDetailsService userDetailsService();
    Boolean verifyToken(String token);
    Boolean verifyForgetToken(String token);
    UserDto convertToDto(User user);
}
