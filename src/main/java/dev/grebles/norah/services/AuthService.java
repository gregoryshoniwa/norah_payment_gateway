package dev.grebles.norah.services;

import dev.grebles.norah.dto.request.RestPassword;
import dev.grebles.norah.dto.response.*;
import dev.grebles.norah.entities.User;

public interface AuthService {
    User signUp(SignUpRequest signUpRequest);
    User secondaryAdminSignUp(SignUpRequest signUpRequest);
    User adminSignUp(SignUpRequest signUpRequest);
    User merchantSignUp(SignUpTransactionRequest signUpTransactionRequest);
    JWTAuthResponse merchantSignIn(SignInRequest signInRequest);
    JWTAuthResponse signIn(SignInRequest signInRequest);
    JWTAuthResponse refreshToken(RefreshTokenRequest refreshTokenRequest);
    User restPasswordByUserName(RestPassword restPassword);
    User forgetPasswordSend(RestPassword restPassword);
}
