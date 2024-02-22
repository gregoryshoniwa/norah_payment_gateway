package dev.grebles.norah.controllers;

import dev.grebles.norah.controllers.components.ResponseEntityBuilder;
import dev.grebles.norah.controllers.responses.HttpResponse;
import dev.grebles.norah.dto.request.RestPassword;
import dev.grebles.norah.dto.request.UserDto;
import dev.grebles.norah.dto.response.SignUpTransactionRequest;
import dev.grebles.norah.services.AuthService;
import dev.grebles.norah.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
public class UserController {

    private final AuthService authService;
    private final UserService userService;
    private final ResponseEntityBuilder responseEntityBuilder;

    static String path = "/api/v1/user";



    @GetMapping
    public ResponseEntity<String> helloUser(){
        return ResponseEntity.ok("Hello User");
    }

    @PostMapping("/change-password")
    public ResponseEntity<HttpResponse> changeUserPassword(@RequestBody RestPassword restPassword){
        UserDto userDto = userService.convertToDto(authService.restPasswordByUserName(restPassword));
        return responseEntityBuilder.ok("Successfully updated user password",
                userDto, path +"/change-password",
                "POST");
    }


}
