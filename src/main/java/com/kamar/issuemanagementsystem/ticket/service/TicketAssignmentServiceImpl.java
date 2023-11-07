package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.external_resouces.EmailService;
import com.kamar.issuemanagementsystem.ticket.controller.ReferralRequestController;
import com.kamar.issuemanagementsystem.ticket.controller.TicketCreationController;
import com.kamar.issuemanagementsystem.ticket.controller.TicketManagementController;
import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.repository.ReferralRequestRepository;
import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.naming.OperationNotSupportedException;

/**
 * implementation of the ticket assignment service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class TicketAssignmentServiceImpl implements TicketAssignmentService {

    private final UserManagementService userManagementService;
    private final TicketManagementService ticketManagementService;
    private final EmailService emailService;
    private final ReferralRequestRepository referralRequestRepository;
    private final ReferralRequestManagementServiceImpl referralRequestManagementService;


    private UserDetails getAuthenticatedUser(){

        return  (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    private void sendNotification(Ticket ticket){

        /*set the subject and Message*/
        String subject = "Ticket assignment";
        Link ticketLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                TicketManagementController.class).getTicketById(ticket.getTicketId(),
                getAuthenticatedUser())).withRel("the ticket");
        String message = "You have been assigned the ticket #" + ticket.getTicketId() + " " + ticket.getTitle() +
                ". Resolve it before " + ticket.getDeadline() + ". \n" + ticketLink;

        /*send the message*/
        emailService.sendEmail(message, subject, ticket.getAssignedTo().getUsername());

    }



    @Override
    public void assignTo(Ticket ticket) throws OperationNotSupportedException{

        /*check if the user is an employee*/
        if (!userManagementService.checkUserByUsernameAndAuthority(
                ticket.getAssignedTo().getUsername(), Authority.EMPLOYEE))
            throw new OperationNotSupportedException();

        /*assign the ticket*/
        Ticket updatedTicket = ticketManagementService.updateTicket(ticket);

        /*send notification*/
        sendNotification(updatedTicket);
    }

}
