package com.kamar.issuemanagementsystem.external_resouces.service;

import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import com.kamar.issuemanagementsystem.external_resouces.data.AttachmentResourceDto;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.util.List;

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
    public boolean sendEmail(String message, String subject, String email, List<AttachmentResourceDto> attachments) {

        /*configure the email*/

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(mimeMessage,true);
        } catch (MessagingException e) {

            /*log*/
            log.error(e.getMessage());
            return false;
        }


        try
        {
            /*add details*/
            mimeMessageHelper.setFrom(company.email(), company.name());
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setSubject(subject);
            mimeMessageHelper.setTo(email);
            mimeMessageHelper.setText(message, true);

            /*add attachments*/
            if (attachments != null && !attachments.isEmpty()) {

                MimeMessageHelper finalMimeMessageHelper = mimeMessageHelper;
                attachments.forEach(attachmentResourceDto -> {
                    try {
                        finalMimeMessageHelper.addAttachment(attachmentResourceDto.filename(), attachmentResourceDto.attachment());
                    } catch (MessagingException e) {

                        /*log */
                        log.error(e.getMessage());
                    }
                });
            }

        }catch (MessagingException | UnsupportedEncodingException e){

            /*log the exception*/
            log.error(e.getMessage());
        }

        /*send the email*/
        javaMailSender.send(mimeMessage);

        return true;
    }
}
