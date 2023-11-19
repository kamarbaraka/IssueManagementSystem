package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketException;
import com.kamar.issuemanagementsystem.user.entity.User;

import java.util.List;

/**
 * the ticket management contract.
 * @author kamar baraka.*/

public interface TicketManagementService {

    Ticket updateTicket(Ticket ticket);
    Ticket getTicketById(long id);
    List<Ticket> getTicketsByRaisedBy(User user);
    Attachment downloadTicketAttachment(final long ticketId) throws TicketException;
}
