package dev.grebles.norah.dto.request;

import lombok.Data;

@Data
public class TokenDto {
    private String url;
    private String password;
    private String token;
}
