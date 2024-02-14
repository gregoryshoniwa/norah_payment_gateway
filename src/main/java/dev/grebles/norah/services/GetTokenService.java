package dev.grebles.norah.services;

import dev.grebles.norah.dto.TokenDto;

public interface GetTokenService {
    TokenDto createToken(TokenDto tokenDto);
}
