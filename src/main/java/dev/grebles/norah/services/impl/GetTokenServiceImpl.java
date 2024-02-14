package dev.grebles.norah.services.impl;

import dev.grebles.norah.dto.TokenDto;
import dev.grebles.norah.services.GetTokenService;
import org.springframework.stereotype.Service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;
import java.util.Base64;
@Service
public class GetTokenServiceImpl implements GetTokenService {
    public TokenDto createToken(TokenDto tokenDto) {
        try {


            MessageDigest md5 = MessageDigest.getInstance("MD5");
            md5.update(tokenDto.getPassword().getBytes());
            byte[] md5Pass = md5.digest();

            DateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
            dateFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            String formattedDate = dateFormat.format(new Date());
            byte[] urlDateBytes = (tokenDto.getUrl() + formattedDate).getBytes();

            byte[] tokenBytes = new byte[md5Pass.length + urlDateBytes.length];
            System.arraycopy(urlDateBytes, 0, tokenBytes, 0, urlDateBytes.length);
            System.arraycopy(md5Pass, 0, tokenBytes, urlDateBytes.length, md5Pass.length);

            MessageDigest sha256 = MessageDigest.getInstance("SHA-256");
            sha256.update(tokenBytes);
            byte[] tokenHash = sha256.digest();

            String token = Base64.getEncoder().encodeToString(tokenHash);
            tokenDto.setToken(token);
            return tokenDto;
        } catch (Exception e) {
            e.printStackTrace();
            tokenDto.setToken(e.getMessage());
            return tokenDto;
        }
    }


}
