/*
package com.kamar.issuemanagementsystem.attachment.utils;

import com.kamar.issuemanagementsystem.attachment.data.AttachmentDTO;
import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.attachment.exception.AttachmentException;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketException;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

*/
/**
 * implementation of the attachment mapper contract.
 * @author kamar baraka.*//*


@Service
@Log4j2
public record AttachmentMapperImpl() implements AttachmentMapper{
    @Override
    public Attachment multipartToAttachment(MultipartFile multipartFile) throws AttachmentException {

        */
/*get the filename*//*

        String filename = multipartFile.getOriginalFilename();
        */
/*get content-type*//*

        String contentType = multipartFile.getContentType();
        */
/*get the content*//*

        byte[] content = new byte[0];
        try {
            content = multipartFile.getBytes();
        } catch (IOException e) {
            */
/*log and throw*//*

            log.error(e.getMessage());
            throw new AttachmentException(e.getMessage());
        }

        Blob blobContent;
        try {
            blobContent = new SerialBlob(content);
        } catch (SQLException e) {
            */
/*log and throw*//*

            log.error(e.getMessage());
            throw new AttachmentException(e.getMessage());
        }

        */
/*set the attachments*//*

        Attachment attachment = new Attachment();
        attachment.setFilename(filename);
        attachment.setContentType(contentType);
        attachment.setContent(blobContent);

        return attachment;
    }

}
*/
