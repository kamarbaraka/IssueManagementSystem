package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.ticket.exceptions.TicketException;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketSubmissionException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * the ticket submission service.
 * @author kamar baraka.*/

public interface TicketSubmissionService {

    void submitTicket(final long ticketId, @AuthenticationPrincipal UserDetails authenticatedUser)
            throws TicketSubmissionException, TicketException;
}
