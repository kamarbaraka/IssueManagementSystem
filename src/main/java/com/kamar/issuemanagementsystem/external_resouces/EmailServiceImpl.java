package com.kamar.issuemanagementsystem.external_resouces;

import com.google.api.client.util.Value;
import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;

/**
 * implementation of the email service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImpl implements EmailService {

    /*inject the dependencies*/
    private final JavaMailSender javaMailSender;
    private final CompanyProperties company;

    @Override
    public boolean authenticateEmail(String email) {
        return false;
    }

    @Override
    public boolean sendEmail(String message, String subject, String email) {

        /*configure the email*/

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        try
        {
            mimeMessageHelper.setFrom(company.email(), company.name());
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText(message, true);

        }catch (MessagingException | UnsupportedEncodingException e){

            /*log the exception*/
            log.error(e.getMessage());
        }

        /*send the email*/
        javaMailSender.send(mimeMessage);

        return true;
    }
}
