package com.mateo.ecom.backend.service;

import com.mateo.ecom.backend.api.exceptions.EmailFailureException;
import com.mateo.ecom.backend.models.VerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.context.MessageSourceAutoConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {

    private final MessageSource messageSource;
    @Value("${email.from}")
    private String fromAddress;

    @Value("${app.frontend.url}")
    private String frontendUrl;

    private JavaMailSender mailSender;
    public EmailService(JavaMailSender mailSender, MessageSource messageSource) {
        this.mailSender = mailSender;
        this.messageSource = messageSource;
    }
    private SimpleMailMessage makeMailMessage(){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(fromAddress);
        return message;
    }
    public void sendEmail(VerificationToken token) throws EmailFailureException {
        SimpleMailMessage message = makeMailMessage();
        message.setTo(token.getUser().getEmail());
        message.setSubject("Verify your email to activate your account");
        message.setText("Please follow the link below to verify your account.\n" +
                frontendUrl + "/auth/veri fy");
        try{
            mailSender.send(message);
        }catch (MailException ex){
            throw new EmailFailureException();
        }

    }
}
