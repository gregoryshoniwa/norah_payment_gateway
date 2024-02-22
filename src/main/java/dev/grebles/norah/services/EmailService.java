package dev.grebles.norah.services;

public interface EmailService {
    void sendSimpleMailMessage(String name,String to,String token,String template);

    void sendHtmlEmail(String name,String to,String token,String template,
                       String subject);
    void sendHtmlEmailPassword(String name,String to,String token,
                              String template,
                       String subject,String password);

}
