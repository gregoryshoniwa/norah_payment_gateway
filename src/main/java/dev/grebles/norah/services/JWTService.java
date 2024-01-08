package dev.grebles.norah.services;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Date;
import java.util.Map;

public interface JWTService {

    String generateToken(UserDetails userDetails);
    String generateTransactionToken(UserDetails userDetails);
    String extractUsername(String token);
    public Boolean validateToken(String token, UserDetails userDetails);

    String generateRefreshToken(Map<String,Object> extractClaims,
                                UserDetails userDetails);
    String generateTransactionRefreshToken(Map<String,Object> extractClaims,
                                           UserDetails userDetails);
    Date getExpirationDate(String token);

}
