package com.kamar.issuemanagementsystem.external_resouces;


/**
 * the email service contract.
 * @author kamar baraka.*/

public interface EmailService {

    boolean authenticateEmail(String email);

    boolean sendEmail(String message, String subject, String email);
}
