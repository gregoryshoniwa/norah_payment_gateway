package dev.grebles.norah.utils;

import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

public class Utils {
    private static PasswordEncoder passwordEncoder;

    public static void setPasswordEncoder(PasswordEncoder encoder) {
        passwordEncoder = encoder;
    }
    public static String UUID() {
        return UUID.randomUUID().toString();
    }

    public static String formattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    public static String formatEmailMessage(String name,String host,
                                            String token,String template) {

        return "Good Day " + name + ",\n\n Your new account has been " +
                "successfully created. Please click the link below to verify " +
                "your email address and enable you account. \n\n" + getVerificationURL(host,token,template) + "\n\nThe Support Team";
    }

    public static String getVerificationURL(String host,String token,
                                            String template){

        if(Objects.equals(template, "emailTemplate")){
            return host + "/api/v1/auth/user-verification?token=" + token;
        }
        if(Objects.equals(template, "forgotPassword")){
            return host + "/api/v1/auth/forget-password-verification?token=" + token;
        }
       return null;
    }

    public static String generateSecurePassword(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[{]}|;:,<.>?";
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            int randomIndex = random.nextInt(chars.length());
            sb.append(chars.charAt(randomIndex));
        }
        return sb.toString();
    }

    public static String encodePassword(String password) {
        return passwordEncoder.encode(password);
    }

    public static boolean matchesPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

}
