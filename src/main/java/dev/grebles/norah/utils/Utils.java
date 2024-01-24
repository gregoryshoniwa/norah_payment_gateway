package dev.grebles.norah.utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class Utils {
    public static String UUID() {
        return UUID.randomUUID().toString();
    }

    public static String formattedTimestamp() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return LocalDateTime.now().format(formatter);
    }

    public static String formatEmailMessage(String name,String host,
                                            String token) {

        return "Good Day " + name + ",\n\n Your new account has been " +
                "successfully created. Please click the link below to verify " +
                "your email address and enable you account. \n\n" + getVerificationURL(host,token) + "\n\nThe Support Team";
    }

    public static String getVerificationURL(String host,String token){
        return host + "/api/v1/auth/user-verification?token=" + token;
    }


}
