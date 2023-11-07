package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.external_resouces.EmailService;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketSubmissionException;
import com.kamar.issuemanagementsystem.ticket.repository.TicketRepository;
import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * implementation of the ticket submission service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class TicketSubmissionServiceImpl implements TicketSubmissionService {

    private final EmailService emailService;
    private final TicketManagementService ticketManagementService;
    private final TicketRepository ticketRepository;
    private final UserManagementService userManagementService;


    private void submitTicketNotification(final Ticket ticket){

        /*compose the email*/
        String subject = "Ticket Review";
        String message = "Ticket #" + ticket.getTicketId() + " " + ticket.getTitle() +
                ", has been resolved. Please check if it is resolved to your satisfaction and provide the feedback.";
        String messageAdmin = ticket.getAssignedTo().getUsername() + " has submitted ticket #" + ticket.getTicketId() + " " + ticket.getTitle();

        /*send notification to the admins*/
        userManagementService.getUsersByAuthority(Authority.ADMIN).parallelStream()
                .forEach(user -> emailService.sendEmail(messageAdmin, subject, user.getUsername()));

        /*send the email to the user*/
        emailService.sendEmail(message, subject, ticket.getRaisedBy().getUsername());

    }

    @Override
    public void submitTicket(final long ticketId, final @AuthenticationPrincipal UserDetails authenticatedUser)
            throws TicketSubmissionException {

        /*get the ticket*/
        Ticket ticket = ticketManagementService.getTicketById(ticketId);

        /*check who is submitting*/
        if (!authenticatedUser.getUsername().equals(ticket.getAssignedTo().getUsername()))
            throw new TicketSubmissionException("you are not permitted to submit this ticket");

        /*update the status*/
        ticket.setStatus(TicketStatus.SUBMITTED);
        ticketRepository.save(ticket);
        /*notify*/
        this.submitTicketNotification(ticket);
    }
}
