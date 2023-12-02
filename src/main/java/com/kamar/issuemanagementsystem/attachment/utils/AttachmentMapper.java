package com.kamar.issuemanagementsystem.attachment.utils;

import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.attachment.exception.AttachmentException;
import org.springframework.web.multipart.MultipartFile;

/**
 * the attachments mapper contract.
 * @author kamar baraka.*/


public interface AttachmentMapper {

    Attachment multipartToAttachment(MultipartFile multipartFile) throws AttachmentException;
}
