package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.repository.TicketRepository;
import com.kamar.issuemanagementsystem.user.entity.User;
import jdk.jshell.Snippet;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * implementation of the ticket reporting service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class TicketReportingServiceImpl implements TicketReportingService {

    private final TicketRepository ticketRepository;

    @Override
    public List<Ticket> ticketsByStatus(TicketStatus status) {

        /*get tickets by status*/
        return ticketRepository.findTicketsByStatusOrderByCreatedOn(status).orElseThrow();
    }

    @Override
    public List<Ticket> userTicketsByStatus(User user, TicketStatus ticketStatus) {

        /*get tickets*/
        return ticketRepository.findTicketsByAssignedToAndStatus(user, ticketStatus).orElseThrow();
    }

    @Override
    public List<Ticket> getAllTickets() {

        /*get all tickets*/
        return ticketRepository.findAll();
    }
}
