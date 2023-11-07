package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.ticket.data.dto.TicketUserFeedbackDTO;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketFeedbackException;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * the user ticket feedback service.
 * @author kamar baraka.*/

public interface TicketFeedbackService {

    void sendFeedback(TicketUserFeedbackDTO userFeedbackDTO, long ticketId,
                       UserDetails authenticatedUser)throws TicketFeedbackException;
}
