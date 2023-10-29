package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.user.entity.User;

import java.util.List;

/**
 * the ticket reporting service.
 * @author kamar baraka.*/

public interface TicketReportingService {

    List<Ticket> ticketsByStatus(TicketStatus status);
    List<Ticket> userTicketsByStatus(User user, TicketStatus ticketStatus);
    List<Ticket> getAllTickets();
}