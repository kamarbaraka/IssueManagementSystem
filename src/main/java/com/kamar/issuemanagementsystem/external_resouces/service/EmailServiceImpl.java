/*
package com.kamar.issuemanagementsystem.external_resouces.service;

import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import com.kamar.issuemanagementsystem.external_resouces.data.AttachmentResourceDto;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

*/
/**
 * implementation of the email service.
 * @author kamar baraka.*//*


@Service
@RequiredArgsConstructor
@Log4j2
public class EmailServiceImpl implements EmailService {

    */
/*inject the dependencies*//*

    private final JavaMailSender javaMailSender;
    private final CompanyProperties company;

    @Value("${spring.mail.username}")
    private String emailUsername;
    @Value("${spring.mail.password}")
    private String emailPassword;
    @Value("${spring.mail.host}")
    private String emailHost;
    @Value("${spring.mail.port}")
    private int emailPort;

    @Override
    public boolean checkEmailExists(String email) {

        try
        {
            */
/*create the mime message and set the recipient*//*

            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            mimeMessage.setRecipients(Message.RecipientType.TO, email);

            */
/*get the session*//*

            Session session = mimeMessage.getSession();

            */
/*get the transport and connect*//*

            Transport smtpTransport = session.getTransport("smtp");

            smtpTransport.connect(emailHost, emailPort, emailUsername, emailPassword);

            */
/*verify the email exists*//*


            */
/*close the connection*//*

            smtpTransport.close();

            return true;
        }catch (NoSuchProviderException e){

            */
/*log*//*

            log.error(e.getMessage());

        }catch (MessagingException e){

            */
/*check error if contains a code for email doesn't exist*//*

            if (e.getMessage().contains("550 5.1.1")) {
                */
/*log*//*

                log.error(e.getMessage());
                return false;
            }
            */
/*log*//*

            log.error(e.getMessage());
        }

        return false;
    }

    @Override
    public void sendEmail(String message, String subject, String email, List<AttachmentResourceDto> attachments) {

        */
/*configure the email*//*


        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        } catch (MessagingException e) {

            */
/*log*//*

            log.error(e.getMessage());
        }


        try
        {
            */
/*add details*//*

            mimeMessageHelper.setFrom(company.email(), company.name());
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText(message, true);

            */
/*add attachments*//*

            if (attachments != null && !attachments.isEmpty()) {

                MimeMessageHelper finalMimeMessageHelper = mimeMessageHelper;
                attachments.forEach(attachmentResourceDto -> {
                    try {
                        finalMimeMessageHelper.addAttachment(attachmentResourceDto.filename(), attachmentResourceDto.attachment());
                    } catch (MessagingException e) {

                        */
/*log *//*

                        log.error(e.getMessage());
                    }
                });
            }

        }catch (MessagingException | UnsupportedEncodingException e){

            */
/*log the exception*//*

            log.error(e.getMessage());
        }

        */
/*send the email*//*

        javaMailSender.send(mimeMessage);

    }
}
*/
