package com.kamar.issuemanagementsystem.global_configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;

/**
 * the email configuration class.
 * @author kamar baraka.*/

@Configuration
public class GlobalMailConfiguration {

    @Bean
    public SimpleMailMessage mailMessage(){

        /*return a mail message implementation*/
        return new SimpleMailMessage();
    }
}
