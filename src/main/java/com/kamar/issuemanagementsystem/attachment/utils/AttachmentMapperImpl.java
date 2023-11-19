package com.kamar.issuemanagementsystem.attachment.utils;

import com.kamar.issuemanagementsystem.attachment.data.AttachmentDTO;
import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.Blob;
import java.sql.SQLException;

/**
 * implementation of the attachment mapper.
 * @author kamar baraka.*/

@Service
@Log4j2
public record AttachmentMapperImpl() implements AttachmentMapper{
    @Override
    public Attachment dtoToAttachment(AttachmentDTO attachmentDTO)  {

        /*get the filename*/
        String filename = attachmentDTO.attachment().getOriginalFilename();
        /*get content-type*/
        String contentType = attachmentDTO.attachment().getContentType();
        /*get the content*/
        byte[] content = new byte[0];
        try {
            content = attachmentDTO.attachment().getBytes();
        } catch (IOException e) {
            /*log*/
            log.error(e.getMessage());

        }
        Blob blobContent;
        try {
            blobContent = new SerialBlob(content);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        /*set the attachment*/
        Attachment attachment = new Attachment();
        attachment.setFilename(filename);
        attachment.setContentType(contentType);
        attachment.setContent(blobContent);

        return attachment;
    }
}