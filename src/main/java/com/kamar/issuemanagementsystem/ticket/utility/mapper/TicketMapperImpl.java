package com.kamar.issuemanagementsystem.ticket.utility.mapper;

import com.kamar.issuemanagementsystem.attachment.data.AttachmentDTO;
import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.attachment.utils.AttachmentMapper;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketAdminPresentationDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketCreationDTO;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

import static java.util.stream.Collectors.toList;

/**
 * implementation of the ticket mapper.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class TicketMapperImpl implements TicketMapper {

    private final AttachmentMapper attachmentMapper;

    @Override
    public Ticket dtoToEntity(TicketCreationDTO ticketCreationDTO) {
        /*map the dto*/
        Ticket ticket = new Ticket();
        ticket.setTitle(ticketCreationDTO.title());
        ticket.setDescription(ticketCreationDTO.description());

        /*check for attachments*/
        if (!ticketCreationDTO.attachment().isEmpty()) {

            /*enumerate the attachments*/
            List<Attachment> attachments = ticketCreationDTO.attachment().parallelStream()
                    .map(AttachmentDTO::new).map(attachmentMapper::dtoToAttachment).toList();

            /*add attachments to ticket*/
            ticket.getAttachments().addAll(attachments);
        }

        return ticket;
    }

    @Override
    public TicketAdminPresentationDTO entityToDTOAdmin(Ticket ticket) {

        /*get attachments*/
        /*check if ticket has attachment and create a download link if present*/
        Collection<Attachment> attachments = ticket.getAttachments();
        List<byte[]> attachmentStream = new ArrayList<>();

        if (!attachments.isEmpty()) {

            attachmentStream = attachments.parallelStream().map(Attachment::getContent).map(this::convertToBytes).toList();

        }

        if (ticket.getAssignedTo() == null || ticket.getDeadline() == null){
            return new TicketAdminPresentationDTO(
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority().toString(),
                ticket.getStatus().toString(),
                ticket.getRaisedBy().getUsername(),
                "not assigned yet",
                "not assigned yet",
                    attachmentStream);
        }

        /*map the dto*/
        return new TicketAdminPresentationDTO(
                ticket.getTitle(),
                ticket.getDescription(),
                ticket.getPriority().toString(),
                ticket.getStatus().toString(),
                ticket.getRaisedBy().getUsername(),
                ticket.getAssignedTo().getUsername(),
                ticket.getDeadline().toString(),
                attachmentStream
        );

    }

    private byte[] convertToBytes(Blob blob){

        try {
            return blob.getBytes(1, ((int) blob.length()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
