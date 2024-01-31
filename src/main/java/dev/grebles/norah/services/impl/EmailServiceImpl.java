package dev.grebles.norah.services.impl;

import dev.grebles.norah.services.EmailService;
import dev.grebles.norah.utils.Utils;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.BodyPart;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.sql.DataSource;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    public static final String UTF_8_ENCODING = "UTF-8";

    @Value("${spring.mail.verify.host}")
    private String host;
    @Value("${spring.mail.username}")
    private String fromEmail;

    private final JavaMailSender emailSender;
    private final TemplateEngine templateEngine;
    private final ResourceLoader resourceLoader;
    @Override
    @Async
    public void sendSimpleMailMessage(String name, String to, String token) {
        try {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setSubject("Email verification");
            simpleMailMessage.setFrom(fromEmail);
            simpleMailMessage.setTo(to);
            simpleMailMessage.setText(Utils.formatEmailMessage(name,host,token));
            emailSender.send(simpleMailMessage);
        }catch (Exception exception){
            throw new RuntimeException(exception.getMessage());
        }
    }


    @Override
    @Async
    public void sendHtmlEmail(String name, String to, String token) {
        try {
            MimeMessage simpleMailMessage = getMimeMessage();
            MimeMessageHelper mimeMessageHelper =
                    new MimeMessageHelper(simpleMailMessage,true, UTF_8_ENCODING);

            mimeMessageHelper.setPriority(1);
            mimeMessageHelper.setSubject("Email verification");
            mimeMessageHelper.setFrom(fromEmail);
            mimeMessageHelper.setTo(to);
            //mimeMessageHelper.setText(text,true);

            Context context = new Context();
            context.setVariable("name",name);
            context.setVariable("url",Utils.getVerificationURL(host,token));
            String text = templateEngine.process("emailTemplate",context);
            //add html email body
            MimeMultipart mimeMultipart = new MimeMultipart("related");
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(text,"text/html");
            mimeMultipart.addBodyPart(messageBodyPart);
            //add images to email body

            // Load and add images to the MimeMultipart
            String header3 = "static/images/header3.png"; // Adjust the path as
            // needed
            Resource imageResource = resourceLoader.getResource("classpath:" + header3);

            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setDataHandler(new DataHandler(new FileDataSource(imageResource.getFile())));
            messageBodyPart.setHeader("Content-ID", "header3");
            mimeMultipart.addBodyPart(messageBodyPart);

            // Set the MimeMultipart as the content of the MimeMessage
            simpleMailMessage.setContent(mimeMultipart);


            emailSender.send(simpleMailMessage);
        }catch (Exception exception){
            throw new RuntimeException(exception.getMessage());
        }
    }


    private MimeMessage getMimeMessage(){
        return emailSender.createMimeMessage();
    }
    private String getContentId(String filename){
        return "<" + filename + ">";
    }

}
