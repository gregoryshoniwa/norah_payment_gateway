package dev.grebles.norah.dto;

import lombok.Data;

@Data
public class JWTAuthResponse {
    private String token;
    private String refreshToken;
    private String tokenExpiryDate;
    private String refreshTokenExpiryDate;
}
