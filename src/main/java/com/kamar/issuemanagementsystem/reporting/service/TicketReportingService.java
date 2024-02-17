package com.kamar.issuemanagementsystem.reporting.service;

import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;

import java.util.List;

/**
 * the ticket reporting service.
 * @author kamar baraka.*/

public interface TicketReportingService {

    List<Ticket> ticketsByStatus(TicketStatus status);
    List<Ticket> userTicketsByStatus(UserEntity userEntity, TicketStatus ticketStatus);
    List<Ticket> getAllTickets();
}
