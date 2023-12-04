package com.kamar.issuemanagementsystem.ticket.utility.util;

import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.external_resouces.data.AttachmentResourceDto;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;

import java.sql.Blob;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

/**
 * implementation of the ticket utilities contract.
 * @author kamar baraka.*/

@Service
public record TicketUtilitiesImpl() implements TicketUtilities{
    @Override
    public List<AttachmentResourceDto> getTicketAttachments(Ticket ticket) {

        Collection<Attachment> existingAttachments = ticket.getAttachments();
        List<AttachmentResourceDto> attachments;
        if (!existingAttachments.isEmpty()) {

            /*convert all attachments to bytes[] the to byte array resource */
            attachments = existingAttachments.stream()
                    .map(attachment -> {

                        /*convert to attachments resource dto*/
                        return new AttachmentResourceDto(attachment.getFilename(), new ByteArrayResource(
                                convertBlobToBytes(attachment.getContent())
                        ));
                    }).toList();

        } else {
            attachments = null;
        }

        return attachments;
    }

    @Override
    public byte[] convertBlobToBytes(Blob blob) {

        /*convert blob to bytes*/
        try {
            return blob.getBytes(1, ((int) blob.length()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
