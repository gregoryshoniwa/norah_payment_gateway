package dev.grebles.norah.services.impl;

import dev.grebles.norah.dto.request.UserDto;
import dev.grebles.norah.entities.Confirmation;
import dev.grebles.norah.entities.PasswordReset;
import dev.grebles.norah.entities.User;
import dev.grebles.norah.repository.ConfirmationRepository;
import dev.grebles.norah.repository.PasswordRestRepository;
import dev.grebles.norah.repository.UserRepository;
import dev.grebles.norah.services.EmailService;
import dev.grebles.norah.services.UserService;
import dev.grebles.norah.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final ConfirmationRepository confirmationRepository;
    private final PasswordRestRepository passwordRestRepository;
    private final EmailService emailService;
    @Autowired
    @Lazy
    private PasswordEncoder passwordEncoder;

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

    @Override
    public Boolean verifyForgetToken(String token) {

        PasswordReset passwordReset = passwordRestRepository.findByToken(token);

        if(passwordReset == null)
            return Boolean.FALSE;
        User user = userRepository.findByEmailIgnoreCase(passwordReset.getUser().getEmail());

        if(user == null)
            return Boolean.FALSE;

        String tempPassword = Utils.generateSecurePassword(10);

        user.setActivated(true);
        user.setPassword(passwordEncoder.encode(tempPassword));
        passwordReset.setUpdatedOn(LocalDateTime.now());
        userRepository.save(user);
        passwordRestRepository.save(passwordReset);

        emailService.sendHtmlEmailPassword(user.getFirstName()
                        +" "+user.getLastName(),
                user.getEmail(),token,"tempPassword",
                "Temporary User Password",tempPassword);


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
