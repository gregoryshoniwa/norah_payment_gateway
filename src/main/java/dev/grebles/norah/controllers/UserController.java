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
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;

    @GetMapping
    public ResponseEntity<String> helloUser(){
        return ResponseEntity.ok("Hello User");
    }


}
