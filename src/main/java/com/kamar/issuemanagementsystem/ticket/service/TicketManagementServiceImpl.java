package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.repository.TicketRepository;
import com.kamar.issuemanagementsystem.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * implementation of the ticket management.\
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class TicketManagementServiceImpl implements TicketManagementService {

    private final TicketRepository ticketRepository;

    @Transactional
    @Override
    public Ticket updateTicket(Ticket ticket) {

        /*check if exists*/
        Ticket savedTicket = ticketRepository.findById(ticket.getTicketId()).orElseThrow();
        ticket.setTicketId(savedTicket.getTicketId());
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
}
