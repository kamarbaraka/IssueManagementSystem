package com.kamar.issuemanagementsystem.external_resouces.service;


import com.kamar.issuemanagementsystem.external_resouces.data.AttachmentResourceDto;

import java.util.List;

/**
 * the email service contract.
 * @author kamar baraka.*/

public interface EmailService {

    boolean authenticateEmail(String email);

    boolean sendEmail(String message, String subject, String email, List<AttachmentResourceDto> attachments);
}
