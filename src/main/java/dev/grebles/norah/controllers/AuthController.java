package dev.grebles.norah.controllers;

import dev.grebles.norah.controllers.components.ResponseEntityBuilder;
import dev.grebles.norah.controllers.responses.HttpResponse;
import dev.grebles.norah.dto.request.RestPassword;
import dev.grebles.norah.dto.request.TokenDto;
import dev.grebles.norah.dto.request.UserDto;
import dev.grebles.norah.dto.response.JWTAuthResponse;
import dev.grebles.norah.dto.response.RefreshTokenRequest;
import dev.grebles.norah.dto.response.SignInRequest;
import dev.grebles.norah.dto.response.SignUpRequest;
import dev.grebles.norah.services.AuthService;
import dev.grebles.norah.services.GetTokenService;
import dev.grebles.norah.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final GetTokenService getTokenService;

    private final ResponseEntityBuilder responseEntityBuilder;
    static String path = "/api/v1/auth";


    @GetMapping
    public ResponseEntity<String> helloNorah(){
        return ResponseEntity.ok("Online");
    }
    @PostMapping("/sign-in")
    public ResponseEntity<JWTAuthResponse> signIn(@RequestBody SignInRequest signInRequest){
        return ResponseEntity.ok(authService.signIn(signInRequest));
    }
    @PostMapping("/merchant-sign-in")
    public ResponseEntity<JWTAuthResponse> merchantSignIn(@RequestBody SignInRequest signInRequest){
        return ResponseEntity.ok(authService.merchantSignIn(signInRequest));
    }
    @PostMapping("/refresh-token")
    public ResponseEntity<JWTAuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest){
        return ResponseEntity.ok(authService.refreshToken(refreshTokenRequest));
    }
    @PostMapping("/iveri-token")
    public ResponseEntity<TokenDto> iveriToken(@RequestBody TokenDto tokenDto){
        return ResponseEntity.ok(getTokenService.createToken(tokenDto));
    }
    @PostMapping("/admin-sign-up")
    public ResponseEntity<HttpResponse> adminSignUp(@RequestBody SignUpRequest signUpRequest){
        UserDto userDto = userService.convertToDto(authService.adminSignUp(signUpRequest));
        return responseEntityBuilder.created(userDto, path +"/admin-sign-up", "POST");
    }
    @GetMapping("/user-verification")
    public ResponseEntity<HttpResponse> confirmNewUser(@RequestParam("token") String token){
        Boolean isSuccess = userService.verifyToken(token);
        if(isSuccess){
            return responseEntityBuilder.ok("Successfully verified new user",
                    Map.of("success", true), path +"/user-verification", "GET");
        }else {
            throw new RuntimeException("Invalid Confirmation Token");
        }

    }

    @GetMapping("/forget-password-verification")
    public ResponseEntity<HttpResponse> confirmForgotPassword(@RequestParam(
            "token") String token){
        Boolean isSuccess = userService.verifyForgetToken(token);
        if(isSuccess){
            return responseEntityBuilder.ok("Successfully sent temporary " +
                            "password",
                    Map.of("success", true), path +"/forget-password-verification", "GET");
        }else {
            throw new RuntimeException("Invalid Password Reset Token");
        }

    }

    @PostMapping("/forgot-password")
    public ResponseEntity<HttpResponse> forgetPassword(@RequestBody RestPassword restPassword){
        UserDto userDto = userService.convertToDto(authService.forgetPasswordSend(restPassword));
        return responseEntityBuilder.ok("Successfully send forget password " +
                        "confirmation",userDto, path +"/change-password",
                "POST");
    }

}
