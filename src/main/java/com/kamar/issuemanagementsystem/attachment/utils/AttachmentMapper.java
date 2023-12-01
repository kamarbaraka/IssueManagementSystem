package com.kamar.issuemanagementsystem.attachment.utils;

import com.kamar.issuemanagementsystem.attachment.data.AttachmentDTO;
import com.kamar.issuemanagementsystem.attachment.entity.Attachment;

import java.io.IOException;

/**
 * the attachments mapper contract.
 * @author kamar baraka.*/


public interface AttachmentMapper {

    Attachment dtoToAttachment(AttachmentDTO attachmentDTO) ;
}
