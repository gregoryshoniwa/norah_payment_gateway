package dev.grebles.norah.services.impl;

import dev.grebles.norah.dto.UserDto;
import dev.grebles.norah.entities.Confirmation;
import dev.grebles.norah.entities.User;
import dev.grebles.norah.repository.ConfirmationRepository;
import dev.grebles.norah.repository.UserRepository;
import dev.grebles.norah.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;

    @Override
    public UserDetailsService userDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username){
                return userRepository.findByEmail(username)
                        .orElseThrow(() -> new UsernameNotFoundException("User not found"));
            }
        };
    }

    @Override
    public Boolean verifyToken(String token) {
        Confirmation confirmation = confirmationRepository.findByToken(token);

        if(confirmation == null)
            return Boolean.FALSE;
        User user = userRepository.findByEmailIgnoreCase(confirmation.getUser().getEmail());

        if(user == null)
            return Boolean.FALSE;

        user.setActivated(true);
        confirmation.setUpdatedOn(LocalDateTime.now());
        userRepository.save(user);
        confirmationRepository.save(confirmation);
        return Boolean.TRUE;
    }

    public UserDto convertToDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setRole(user.getRole().toString());
        userDto.setUserName(user.getUsername());
        userDto.setCompanyName(user.getCompanyName());
        userDto.setEnabled(user.isActivated());
        return userDto;
    }

}
