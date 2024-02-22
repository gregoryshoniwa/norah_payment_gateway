package dev.grebles.norah.dto.response;

import lombok.Data;

@Data
public class SignInRequest {
    private String email;
    private String password;
}
