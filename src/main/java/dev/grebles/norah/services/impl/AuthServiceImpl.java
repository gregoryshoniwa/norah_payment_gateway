package dev.grebles.norah.services.impl;

import dev.grebles.norah.dto.*;
import dev.grebles.norah.entities.Merchant;
import dev.grebles.norah.entities.User;
import dev.grebles.norah.enums.MerchantStatus;
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

import java.time.LocalDateTime;
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


    /**
     * Sign up a new user and set the primary user and company to the current
     * authenticated user
     * @param signUpRequest DTO for the sign-up request
     * @return the new user
     * @throws IllegalArgumentException if the current authenticated user is invalid
     */
    public User signUp(SignUpRequest signUpRequest){
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        //get the username from the current authenticated user
        String authenticatedUser = jwtService.getAuthenticatedUser();
        User currentUser = userRepository.findByEmail(authenticatedUser).orElseThrow(() -> new IllegalArgumentException("Invalid User"));

        //set the primary user will the user id of the current authenticated user
        user.setPrimaryUser(currentUser.getId());
        user.setCompanyName(currentUser.getCompanyName());

        return userRepository.save(user);
    }

    /**
     * Sign up a new admin-user and set the primary user to 0 and company
     * @param signUpRequest DTO for the sign-up request
     * @return the new user
     */
    public User adminSignUp(SignUpRequest signUpRequest){
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setRole(Role.ADMIN);
        user.setCompanyName(signUpRequest.getCompanyName());
        user.setPrimaryUser(0L);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));
        return userRepository.save(user);
    }

    /**
     * Sign up a new secondary admin-user and set the primary user to the current
     * authenticated user and company
     * @param signUpRequest DTO for the sign-up request
     * @return the new user
     * @throws IllegalArgumentException if the current authenticated user is invalid
     */
    public User secondaryAdminSignUp(SignUpRequest signUpRequest){
        User user = new User();
        user.setFirstName(signUpRequest.getFirstName());
        user.setLastName(signUpRequest.getLastName());
        user.setEmail(signUpRequest.getEmail());
        user.setRole(Role.ADMIN);
        user.setPassword(passwordEncoder.encode(signUpRequest.getPassword()));

        //get the username from the current authenticated user
        String authenticatedUser = jwtService.getAuthenticatedUser();
        User currentUser = userRepository.findByEmail(authenticatedUser).orElseThrow(() -> new IllegalArgumentException("Invalid User"));

        //set the primary user will the user id of the current authenticated user
        user.setPrimaryUser(currentUser.getId());
        user.setCompanyName(currentUser.getCompanyName());

        return userRepository.save(user);
    }

    /**
     * Sign up a new merchant, merchant-user and set the primary user to the
     * current authenticated user and company
     * @param signUpTransactionRequest DTO for the sign-up request
     * @return the new user with the merchant secret as the password
     * @throws IllegalArgumentException if the current authenticated user is invalid
     * @throws IllegalArgumentException if the merchant email already exists
     */
    public User merchantSignUp(SignUpTransactionRequest signUpTransactionRequest){
        String merchantSecret = UUID.randomUUID().toString();
        User user = new User();
        user.setFirstName(signUpTransactionRequest.getMerchantName());
        user.setLastName("Merchant Transaction API");

        // generate unix date to be used as the merchant email
        long unixTime = java.time.Instant.now().getEpochSecond();
        user.setEmail(unixTime + "@norah.com");

        user.setRole(Role.TRANSACTION);
        user.setPassword(passwordEncoder.encode(merchantSecret));

        //get the username from the current authenticated user
        String authenticatedUser = jwtService.getAuthenticatedUser();
        User currentUser = userRepository.findByEmail(authenticatedUser).orElseThrow(() -> new IllegalArgumentException("Invalid User"));

        //set the primary user will the user id of the current authenticated user
        user.setPrimaryUser(currentUser.getId());
        user.setCompanyName(currentUser.getCompanyName());

        Merchant merchant = new Merchant();
        merchant.setMerchantName(signUpTransactionRequest.getMerchantName());
        merchant.setMerchantAddress(signUpTransactionRequest.getMerchantAddress());
        merchant.setMerchantPhone(signUpTransactionRequest.getMerchantPhone());
        merchant.setMerchantEmail(signUpTransactionRequest.getMerchantEmail());
        merchant.setMerchantSecret(merchantSecret);
        merchant.setMerchantStatus(MerchantStatus.DEVELOPMENT);
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

    /**
     * Sign in a merchant-user and generate a new transaction merchant token
     * and refresh token
     * @param signInRequest DTO for the sign-in request
     * @return the new token and refresh token
     * @throws IllegalArgumentException if the credentials are invalid
     */
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

    /**
     * Sign in a user and generate a new token and refresh token
     * @param signInRequest DTO for the sign-in request
     * @return the new token and refresh token
     * @throws IllegalArgumentException if the credentials are invalid
     */
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

    /**
     * Refresh the token and generate a new token and refresh token
     * @param refreshTokenRequest DTO for the refresh token request
     * @return the new token and refresh token
     * @throws IllegalArgumentException if the token is invalid
     */
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
