package com.kamar.issuemanagementsystem.reporting.service;

import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.user.entity.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * the ticket reporting service.
 * @author kamar baraka.*/

public interface TicketReportingService {

    List<Ticket> ticketsByStatus(TicketStatus status, UserDetails userDetails);
    List<Ticket> userTicketsByStatus(User user, TicketStatus ticketStatus);
    List<Ticket> getAllTickets(UserDetails authenticatedUser);
}
