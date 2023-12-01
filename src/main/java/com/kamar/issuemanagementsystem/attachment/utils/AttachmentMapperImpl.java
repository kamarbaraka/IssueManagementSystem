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
 * implementation of the attachments mapper.
 * @author kamar baraka.*/

@Service
@Log4j2
public record AttachmentMapperImpl() implements AttachmentMapper{
    @Override
    public Attachment dtoToAttachment(AttachmentDTO attachmentDTO)  {

        /*get the filename*/
        String filename = attachmentDTO.originalFilename();
        /*get content-type*/
        String contentType = attachmentDTO.contentType();
        /*get the content*/
        byte[] content = attachmentDTO.content();

        Blob blobContent;
        try {
            blobContent = new SerialBlob(content);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        /*set the attachments*/
        Attachment attachment = new Attachment();
        attachment.setFilename(filename);
        attachment.setContentType(contentType);
        attachment.setContent(blobContent);

        return attachment;
    }
}
