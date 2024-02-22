package dev.grebles.norah.dto.response;

import lombok.Data;

@Data
public class JWTAuthResponse {
    private String token;
    private String refreshToken;
    private String tokenExpiryDate;
    private String refreshTokenExpiryDate;
    private Long user_id;
    private String role;
    private String fullName;
    private String email;
}
