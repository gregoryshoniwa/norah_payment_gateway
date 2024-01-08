package dev.grebles.norah.controllers;

import dev.grebles.norah.dto.JWTAuthResponse;
import dev.grebles.norah.dto.SignInRequest;
import dev.grebles.norah.dto.SignUpRequest;
import dev.grebles.norah.dto.SignUpTransactionRequest;
import dev.grebles.norah.entities.User;
import dev.grebles.norah.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;

    @GetMapping
    public ResponseEntity<String> helloAdmin(){
        return ResponseEntity.ok("Hello Admin");
    }

    @PostMapping("/user-sign-up")
    public ResponseEntity<User> signUp(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authService.signUp(signUpRequest));
    }

    @PostMapping("/secondary-admin-sign-up")
    public ResponseEntity<User> secondaryAdminSignUp(@RequestBody SignUpRequest signUpRequest){
        return ResponseEntity.ok(authService.secondaryAdminSignUp(signUpRequest));
    }

    @PostMapping("/create-merchant-account")
    public ResponseEntity<User> createMerchantAccount(@RequestBody SignUpTransactionRequest signUpTransactionRequest){
        return ResponseEntity.ok(authService.merchantSignUp(signUpTransactionRequest));
    }

}
