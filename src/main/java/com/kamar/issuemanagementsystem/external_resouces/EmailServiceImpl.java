package com.kamar.issuemanagementsystem.external_resouces;

import lombok.RequiredArgsConstructor;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * implementation of the email service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    /*inject the dependencies*/
    private final JavaMailSender javaMailSender;
    private final SimpleMailMessage mailMessage;

    @Override
    public boolean authenticateEmail(String email) {
        return false;
    }

    @Override
    public boolean sendEmail(String message, String subject, String email) {

        /*configure the email*/
        mailMessage.setTo(email);
        mailMessage.setFrom("no-reply@example.com");
        mailMessage.setSubject(subject);
        mailMessage.setText(message);

        /*send the email*/
        javaMailSender.send(mailMessage);

        return true;
    }
}
