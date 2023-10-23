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

    private void acceptedRequestNotification(Ticket ticket){

        /*set the message and subject*/
        String subject = "Reassigned";

        Link ticketLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TicketManagementController.class)
                        .getTicketById(ticket.getTicketId(), getAuthenticatedUser())).withRel("ticket");

        String message = "the ticket #" + ticket.getTicketId() + " " + ticket.getTitle()
                + "is assigned to you upon accepting referral request. Resolve it before "
                + ticket.getDeadline() + "\n" + ticketLink;

        /*send the message*/
        emailService.sendEmail(message, subject, ticket.getAssignedTo().getUsername());


    }

    private void receiveAcceptedRequestNotification(ReferralRequest referralRequest){

        /*set the email*/
        String subject = "Request Accepted";


        String message = referralRequest.getTo().getUsername() + " accepted your referral request for ticket #"
                + referralRequest.getRefferedTicket().getTicketId() + " "
                + referralRequest.getRefferedTicket().getTitle() + "\n" ;

        /*send message*/
        emailService.sendEmail(message, subject, referralRequest.getFrom().getUsername());
    }

    public void receiveRejectedRequestNotification(ReferralRequest referralRequest){

        /*set the email*/
        String subject = "Request Rejected";
        String message = referralRequest.getTo().getUsername() + " rejected your referral request for ticket #"
                + referralRequest.getRefferedTicket().getTicketId() + " "
                + referralRequest.getRefferedTicket().getTitle();

        /*send notification*/
        emailService.sendEmail(message, subject, referralRequest.getFrom().getUsername());
    }

    private void sendReferralRequestNotification(ReferralRequest referralRequest){

        /*set up the email*/
        String subject = "Referral Request";
        /*add links*/
        Link acceptRequest = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                ReferralRequestController.class).acceptReferralRequest(
                        true, referralRequest.getRequestId())).withRel("accept");

        Link rejectRequest = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ReferralRequestController.class)
                        .acceptReferralRequest(false, referralRequest.getRequestId())).withRel("reject");

        /*set the message*/
        String message = referralRequest.getFrom().getUsername() + "requests to refer to you the ticket #"
                + referralRequest.getRefferedTicket().getTicketId() + " "
                + referralRequest.getRefferedTicket().getTitle() + ". \n" + acceptRequest+ "\n"+ rejectRequest;

        /*send message*/
        emailService.sendEmail(message, subject, referralRequest.getTo().getUsername());
    }

    private void sendReferralRequest(ReferralRequest referralRequest){

        /*persist the request*/
        ReferralRequest savedRequest = referralRequestRepository.save(referralRequest);

        /*send a referral request*/
        sendReferralRequestNotification(savedRequest);

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

    @Override
    public void referTicketTo(Ticket ticket, String to) {

        /*get data*/
        User fromUser = ticket.getAssignedTo();
        User toUser = userManagementService.getUserByUsername(to);

        /*construct a referral request*/
        ReferralRequest referralRequest = new ReferralRequest();
        referralRequest.setRefferedTicket(ticket);
        referralRequest.setFrom(fromUser);
        referralRequest.setTo(toUser);

        /*send a referral request*/
        sendReferralRequest(referralRequest);

    }

    public void respondToReferralRequest(ReferralRequest referralRequest, boolean response){

        if (!response){

            /*notify the referrer*/
            receiveRejectedRequestNotification(referralRequest);
        }

        /*notify the referrer*/
        receiveAcceptedRequestNotification(referralRequest);
        acceptedRequestNotification(referralRequest.getRefferedTicket());
    }

}
