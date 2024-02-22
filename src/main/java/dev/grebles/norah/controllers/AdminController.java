package dev.grebles.norah.controllers;

import dev.grebles.norah.controllers.components.ResponseEntityBuilder;
import dev.grebles.norah.controllers.responses.HttpResponse;
import dev.grebles.norah.dto.request.RestPassword;
import dev.grebles.norah.dto.request.UserDto;
import dev.grebles.norah.dto.response.SignUpRequest;
import dev.grebles.norah.dto.response.SignUpTransactionRequest;
import dev.grebles.norah.services.AuthService;
import dev.grebles.norah.services.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AuthService authService;
    private final UserService userService;
    private final ResponseEntityBuilder responseEntityBuilder;
    static String path = "/api/v1/admin";

    @GetMapping
    public ResponseEntity<String> helloAdmin(){
        return ResponseEntity.ok("Hello Admin");
    }

    @PostMapping("/user-sign-up")
    public ResponseEntity<HttpResponse> signUp(@RequestBody SignUpRequest signUpRequest, HttpServletRequest request){
        UserDto userDto = userService.convertToDto(authService.signUp(signUpRequest));
        return responseEntityBuilder.created(userDto, path +"/user-sign-up", "POST");
    }


    @PostMapping("/secondary-admin-sign-up")
    public ResponseEntity<HttpResponse> secondaryAdminSignUp(@RequestBody SignUpRequest signUpRequest){
        UserDto userDto = userService.convertToDto(authService.secondaryAdminSignUp(signUpRequest));
        return responseEntityBuilder.created(userDto, path +"/secondary-admin-sign-up", "POST");
    }

    @PostMapping("/create-merchant-account")
    public ResponseEntity<HttpResponse> createMerchantAccount(@RequestBody SignUpTransactionRequest signUpTransactionRequest){
        UserDto userDto = userService.convertToDto(authService.merchantSignUp(signUpTransactionRequest));
        return responseEntityBuilder.created(userDto, path +"/create-merchant-account", "POST");
    }

    @PostMapping("/change-password")
    public ResponseEntity<HttpResponse> changeAdminPassword(@RequestBody RestPassword restPassword){
        UserDto userDto = userService.convertToDto(authService.restPasswordByUserName(restPassword));
        return responseEntityBuilder.ok("Successfully updated admin user " +
                        "password",userDto, path +"/change-password",
                "POST");
    }

}
