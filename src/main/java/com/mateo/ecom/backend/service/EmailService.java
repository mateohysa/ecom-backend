package com.mateo.ecom.backend.service;

import com.mateo.ecom.backend.api.exceptions.EmailFailureException;
import com.mateo.ecom.backend.models.AppUser;
import com.mateo.ecom.backend.models.VerificationToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService{

        @Value("${email.from}")
        private String fromAddress;
        @Value("${app.frontend.url}")
        private String url;
        private JavaMailSender javaMailSender;


        public EmailService(JavaMailSender javaMailSender) {
            this.javaMailSender = javaMailSender;
        }

        private SimpleMailMessage makeMailMessage() {
            SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
            simpleMailMessage.setFrom(fromAddress);
            return simpleMailMessage;
        }


        public void sendVerificationEmail(VerificationToken verificationToken) throws EmailFailureException {
            SimpleMailMessage message = makeMailMessage();
            message.setTo(verificationToken.getUser().getEmail());
            message.setSubject("Verify your email to activate your account.");
            message.setText("Please follow the link below to verify your email to activate your account.\n" +
                    url + "/auth/verify?token=" + verificationToken.getToken());
            try {
                javaMailSender.send(message);
            } catch (MailException ex) {
                throw new EmailFailureException();
            }
        }

    public void sendPasswordResetEmail(AppUser appUser, String token) throws EmailFailureException {
        SimpleMailMessage message = makeMailMessage();
        message.setTo(appUser.getEmail());
        message.setSubject("Password Reset.");
        message.setText("Please follow the link below to reset the password for your account.\n" +
                url + "/auth/reset?token=" + token);
        try {
            javaMailSender.send(message);
        } catch (MailException ex) {
            throw new EmailFailureException();
        }
    }

    }
