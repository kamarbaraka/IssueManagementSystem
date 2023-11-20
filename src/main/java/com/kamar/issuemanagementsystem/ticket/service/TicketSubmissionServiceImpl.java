package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.external_resouces.service.EmailService;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketSubmissionException;
import com.kamar.issuemanagementsystem.ticket.repository.TicketRepository;
import com.kamar.issuemanagementsystem.user.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * implementation of the ticket submission service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Transactional
public class TicketSubmissionServiceImpl implements TicketSubmissionService {

    private final EmailService emailService;
    private final TicketManagementService ticketManagementService;
    private final TicketRepository ticketRepository;
    private final UserManagementService userManagementService;
    private final  CompanyProperties company;


    private void submitTicketNotification(final Ticket ticket){

        /*compose the email*/
        String subject = "Ticket Review";
        String message = "Dear "+ ticket.getRaisedBy().getUsername()+ ", your ticket #"+ ticket.getTicketId()+ " \""+
                ticket.getTitle()+ "\", has been resolved. Please check if it is resolved to your satisfaction and provide the feedback.<>br"+
                company.endTag();

        String messageAdmin = ticket.getAssignedTo().getUsername() + " has submitted ticket #"+ ticket.getTicketId()+
                " \""+ ticket.getTitle()+ "\".<br>"+
                company.endTag();

        /*send notification to the admins*/
        userManagementService.getUsersByAuthority(UserAuthority.getFor("admin")).parallelStream()
                .forEach(admin -> emailService.sendEmail(messageAdmin, subject, admin.getUsername(), null));

        /*send notification to the department*/
        emailService.sendEmail(messageAdmin, subject, ticket.getDepartmentAssigned().getDepartmentEmail(), null);

        /*send the email to the user*/
        emailService.sendEmail(message, subject, ticket.getRaisedBy().getUsername(), null);

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
