package dev.grebles.norah.services;

import dev.grebles.norah.dto.*;
import dev.grebles.norah.entities.User;

public interface AuthService {
    User signUp(SignUpRequest signUpRequest);
    User secondaryAdminSignUp(SignUpRequest signUpRequest);
    User adminSignUp(SignUpRequest signUpRequest);
    User merchantSignUp(SignUpTransactionRequest signUpTransactionRequest);
    JWTAuthResponse merchantSignIn(SignInRequest signInRequest);
    JWTAuthResponse signIn(SignInRequest signInRequest);
    JWTAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
}
