package dev.grebles.norah.controllers;

import dev.grebles.norah.controllers.responses.HttpResponse;
import dev.grebles.norah.dto.*;
import dev.grebles.norah.services.AuthService;
import dev.grebles.norah.services.UserService;
import dev.grebles.norah.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;
    private final UserService userService;

    @GetMapping
    public ResponseEntity<String> helloAdmin(){
        return ResponseEntity.ok("Hello Admin");
    }

    @PostMapping("/user-sign-up")
    public ResponseEntity<HttpResponse> signUp(@RequestBody SignUpRequest signUpRequest){
        UserDto userDto = userService.convertToDto(authService.signUp(signUpRequest));
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timestamp(Utils.formattedTimestamp())
                        .data(Map.of("user",userDto))
                        .message("Successfully created new user")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .requestMethod("POST")
                        .path("/api/v1/admin/user-sign-up")
                        .build()
        );
    }

    @PostMapping("/secondary-admin-sign-up")
    public ResponseEntity<HttpResponse> secondaryAdminSignUp(@RequestBody SignUpRequest signUpRequest){

        UserDto userDto = userService.convertToDto(authService.secondaryAdminSignUp(signUpRequest));
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timestamp(Utils.formattedTimestamp())
                        .data(Map.of("user",userDto))
                        .message("Successfully created new secondary user")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .requestMethod("POST")
                        .path("/api/v1/admin/secondary-admin-sign-up")
                        .build()
        );
    }

    @PostMapping("/create-merchant-account")
    public ResponseEntity<HttpResponse> createMerchantAccount(@RequestBody SignUpTransactionRequest signUpTransactionRequest){

        UserDto userDto = userService.convertToDto(authService.merchantSignUp(signUpTransactionRequest));
        return ResponseEntity.created(URI.create("")).body(
                HttpResponse.builder()
                        .timestamp(Utils.formattedTimestamp())
                        .data(Map.of("user",userDto))
                        .message("Successfully created new merchant api user")
                        .status(HttpStatus.CREATED)
                        .statusCode(HttpStatus.CREATED.value())
                        .requestMethod("POST")
                        .path("/api/v1/admin/create-merchant-account")
                        .build()
        );

    }

}
