package com.kamar.issuemanagementsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailMessage;
import org.springframework.mail.SimpleMailMessage;

/**
 * the email configuration class.
 * @author kamar baraka.*/

@Configuration
public class EmailConfig {

    @Bean
    public SimpleMailMessage mailMessage(){

        /*return a mail message implementation*/
        return new SimpleMailMessage();
    }
}
