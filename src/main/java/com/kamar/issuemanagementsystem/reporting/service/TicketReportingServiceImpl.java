package com.kamar.issuemanagementsystem.reporting.service;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.repository.TicketRepository;
import com.kamar.issuemanagementsystem.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * implementation of the ticket reporting service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Transactional
public class TicketReportingServiceImpl implements TicketReportingService {

    private final TicketRepository ticketRepository;
    private final UserAuthorityUtility userAuthorityUtility;

    @Override
    public List<Ticket> ticketsByStatus(TicketStatus status, UserDetails authenticatedUser) {

        /*get tickets by status*/
        List<Ticket> ticketsByStatus = ticketRepository.findTicketsByStatusOrderByCreatedOn(status).orElseThrow();

        if (authenticatedUser.getAuthorities().contains(userAuthorityUtility.getFor("employee")) &&
                (!authenticatedUser.getAuthorities().contains(userAuthorityUtility.getFor("department_admin")))) {

            /*filter the result*/
            return ticketsByStatus.parallelStream().filter(
                    ticket -> ticket.getAssignedTo().getUsername().equals(authenticatedUser.getUsername())
            ).toList();
        }

        return ticketsByStatus;
    }

    @Override
    public List<Ticket> userTicketsByStatus(User user, TicketStatus ticketStatus) {

        /*get tickets*/
        return ticketRepository.findTicketsByAssignedToAndStatus(user, ticketStatus).orElseThrow();
    }

    @Override
    public List<Ticket> getAllTickets(UserDetails authenticatedUser) {

        /*get all tickets*/
        List<Ticket> allTickets = ticketRepository.findAll();

        if (authenticatedUser.getAuthorities().contains(userAuthorityUtility.getFor("employee")) &&
                (authenticatedUser.getAuthorities().contains(userAuthorityUtility.getFor("department_admin")))) {

            /*filter the tickets*/
            return allTickets.parallelStream().filter(
                    ticket -> ticket.getAssignedTo().getUsername().equals(authenticatedUser.getUsername()) ||
                            ticket.getRaisedBy().getUsername().equals(authenticatedUser.getUsername())
            ).toList();
        }

        /*check the authorities*/
        if (authenticatedUser.getAuthorities().contains(userAuthorityUtility.getFor("user")) &&
                (!authenticatedUser.getAuthorities().contains(userAuthorityUtility.getFor("admin")))) {

            /*filter*/
            return allTickets.parallelStream().filter(
                    ticket -> ticket.getRaisedBy().getUsername().equals(authenticatedUser.getUsername())
            ).toList();
        }


        return allTickets;

    }
}
