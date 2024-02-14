package dev.grebles.norah.controllers;

import dev.grebles.norah.controllers.responses.HttpResponse;
import dev.grebles.norah.dto.*;
import dev.grebles.norah.services.AuthService;
import dev.grebles.norah.services.GetTokenService;
import dev.grebles.norah.services.UserService;
import dev.grebles.norah.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final GetTokenService getTokenService;


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
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timestamp(Utils.formattedTimestamp())
                        .data(Map.of("user",userDto))
                        .message("Successfully created new admin user")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .requestMethod("POST")
                        .path("/api/v1/auth/admin-sign-up")
                        .build()
        );
    }
    @GetMapping("/user-verification")
    public ResponseEntity<HttpResponse> confirmNewUser(@RequestParam("token") String token){
        Boolean isSuccess = userService.verifyToken(token);
        return ResponseEntity.ok().body(
                HttpResponse.builder()
                        .timestamp(Utils.formattedTimestamp())
                        .data(Map.of("success",isSuccess))
                        .message("Successfully verified new user")
                        .status(HttpStatus.OK)
                        .statusCode(HttpStatus.OK.value())
                        .requestMethod("GET")
                        .path("/api/v1/auth/user-verification")
                        .build()
        );
    }

}
