package dev.grebles.norah.services.impl;

import dev.grebles.norah.dto.*;
import dev.grebles.norah.entities.Merchant;
import dev.grebles.norah.entities.User;
import dev.grebles.norah.enums.Role;
import dev.grebles.norah.repository.MerchantRepository;
import dev.grebles.norah.repository.UserRepository;
import dev.grebles.norah.services.AuthService;
import dev.grebles.norah.services.JWTService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final MerchantRepository merchantRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JWTService jwtService;

    public User signUp(SignUpRequest signUpRequest){
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        return userRepository.save(user);

    }

    public User merchantSignUp(SignUpTransactionRequest signUpTransactionRequest){
        String merchantSecret = UUID.randomUUID().toString();
        User user = new User();
        user.setFirstName(signUpTransactionRequest.getMerchantName());
        user.setLastName("Merchant Transaction API");
        user.setEmail(signUpTransactionRequest.getMerchantEmail());
        user.setRole(Role.TRANSACTION);
        user.setPassword(passwordEncoder.encode(merchantSecret));

        Merchant merchant = new Merchant();
        merchant.setMerchantName(signUpTransactionRequest.getMerchantName());
        merchant.setMerchantAddress(signUpTransactionRequest.getMerchantAddress());
        merchant.setMerchantPhone(signUpTransactionRequest.getMerchantPhone());
        merchant.setMerchantEmail(signUpTransactionRequest.getMerchantEmail());
        merchant.setMerchantSecret(merchantSecret);
        merchant.setMerchantStatus(signUpTransactionRequest.getMerchantStatus());
        merchant.setMerchantType(signUpTransactionRequest.getMerchantType());
        merchant.setMerchantCountry(signUpTransactionRequest.getMerchantCountry());
        merchant.setMerchantCity(signUpTransactionRequest.getMerchantCity());
        merchant.setMerchantWebsite(signUpTransactionRequest.getMerchantWebsite());
        merchant.setMerchantDescription(signUpTransactionRequest.getMerchantDescription());

        //save the new user and then save the id to the merchant table
        userRepository.save(user);
        merchant.setUser(user);
        merchantRepository.save(merchant);
        user.setPassword(merchantSecret);
        return user;
    }

    public JWTAuthResponse merchantSignIn(SignInRequest signInRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),signInRequest.getPassword()));
        var user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid Credentials"));
        var token = jwtService.generateTransactionToken(user);
        var refreshToken = jwtService.generateTransactionRefreshToken(new HashMap<>(), user);
        var tokenExpiryDate = jwtService.getExpirationDate(token);
        var refreshTokenExpiryDate = jwtService.getExpirationDate(refreshToken);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setToken(token);
        jwtAuthResponse.setRefreshToken(refreshToken);
        jwtAuthResponse.setTokenExpiryDate(tokenExpiryDate.toString());
        jwtAuthResponse.setRefreshTokenExpiryDate(refreshTokenExpiryDate.toString());
        return jwtAuthResponse;
    }

    public JWTAuthResponse signIn(SignInRequest signInRequest){
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getEmail(),signInRequest.getPassword()));
        var user = userRepository.findByEmail(signInRequest.getEmail()).orElseThrow(() -> new IllegalArgumentException("Invalid Credentials"));
        var token = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        var tokenExpiryDate = jwtService.getExpirationDate(token);
        var refreshTokenExpiryDate = jwtService.getExpirationDate(refreshToken);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setToken(token);
        jwtAuthResponse.setRefreshToken(refreshToken);
        jwtAuthResponse.setTokenExpiryDate(tokenExpiryDate.toString());
        jwtAuthResponse.setRefreshTokenExpiryDate(refreshTokenExpiryDate.toString());
        return jwtAuthResponse;
    }

    public JWTAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest){
        String  username =
                jwtService.extractUsername(refreshTokenRequest.getToken());
        User user =
                userRepository.findByEmail(username).orElseThrow(() -> new IllegalArgumentException("Invalid Credentials"));

        if(!jwtService.validateToken(refreshTokenRequest.getToken(),user)){
            throw new IllegalArgumentException("Invalid Token");
        }
        var token = jwtService.generateToken(user);
        var newRefreshToken = jwtService.generateRefreshToken(new HashMap<>(), user);
        var tokenExpiryDate = jwtService.getExpirationDate(token);
        var refreshTokenExpiryDate = jwtService.getExpirationDate(newRefreshToken);

        JWTAuthResponse jwtAuthResponse = new JWTAuthResponse();
        jwtAuthResponse.setToken(token);
        jwtAuthResponse.setRefreshToken(newRefreshToken);
        jwtAuthResponse.setTokenExpiryDate(tokenExpiryDate.toString());
        jwtAuthResponse.setRefreshTokenExpiryDate(refreshTokenExpiryDate.toString());
        return jwtAuthResponse;
    }
}
