package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketException;
import com.kamar.issuemanagementsystem.ticket.repository.TicketRepository;
import com.kamar.issuemanagementsystem.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * implementation of the ticket management.\
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Transactional
public class TicketManagementServiceImpl implements TicketManagementService {

    private final TicketRepository ticketRepository;

    @Override
    public Ticket updateTicket(Ticket ticket) {

        /*check if exists*/
        Ticket savedTicket = ticketRepository.findById(ticket.getTicketId()).orElseThrow();
        savedTicket.setAssignedTo(ticket.getAssignedTo());
        savedTicket.setPriority(ticket.getPriority());
        savedTicket.setDeadline(ticket.getDeadline());
        savedTicket.setStatus(TicketStatus.ASSIGNED);
        return ticketRepository.save(ticket);
    }

    @Override
    public Ticket getTicketById(long id) {

        /*get the ticket by id*/
        return ticketRepository.findById(id).orElseThrow();
    }

    @Override
    public List<Ticket> getTicketsByRaisedBy(User user) {

        /*get tickets*/
        return ticketRepository.findTicketsByRaisedBy(user).orElseThrow();
    }

    public Attachment downloadTicketAttachment(final long ticketId) throws TicketException{

        /*get the ticket*/
        Optional<Ticket> optSavedTicket = ticketRepository.findById(ticketId);

        if (optSavedTicket.isEmpty()) {
            throw new TicketException("ticket not found");
        }

        Ticket ticket = optSavedTicket.get();
        Collection<Attachment> attachments = ticket.getAttachments();
        Optional<Attachment> optAttachments = attachments.parallelStream().findFirst();

        return optAttachments.orElse(null);

    }
}
