package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.external_resouces.data.AttachmentResourceDto;
import com.kamar.issuemanagementsystem.external_resouces.service.EmailService;
import com.kamar.issuemanagementsystem.ticket.controller.TicketManagementController;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.utility.util.TicketUtilities;
import com.kamar.issuemanagementsystem.user.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.OperationNotSupportedException;
import java.util.List;

/**
 * implementation of the ticket assignment service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Transactional
public class TicketAssignmentServiceImpl implements TicketAssignmentService {

    private final UserManagementService userManagementService;
    private final TicketManagementService ticketManagementService;
    private final EmailService emailService;
    private final UserAuthorityUtility userAuthorityUtility;
    private final CompanyProperties company;
    private final TicketUtilities ticketUtilities;


    private UserDetails getAuthenticatedUser(){

        return  (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    private void sendNotification(Ticket ticket){

        /*set the subject and Message*/
        String subject = "Ticket assignment";
        Link ticketLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                TicketManagementController.class).getTicketById(ticket.getTicketNumber())).withRel("the ticket");

        String message = "You have been assigned the ticket " + ticket.getTicketNumber()+ " "+ ticket.getTitle() +
                ". Resolve it before "+ "<h4 style=\"color: red;\">"+ ticket.getDeadline()+ "</h4> <br>"+
                "  <h4><a href=\""+ ticketLink.getHref()+ "\" >Ticket</a></h4> <br>"+
                company.endTag();

        /*get attachments*/
        List<AttachmentResourceDto> attachments = ticketUtilities.getTicketAttachments(ticket);

        /*send the message*/
        emailService.sendEmail(message, subject, ticket.getAssignedTo().getUsername(), attachments);

        /*compose and send notification to the raiser*/
        String raiserSubject = "Ticket handling";
        String raiserMessage = "Dear "+ ticket.getRaisedBy().getUsername()+ ",Your ticket " + ticket.getTicketNumber()
                + " \"" + ticket.getTitle() + "\""+
                " is being handled by the " + ticket.getDepartmentAssigned().getDepartmentName()+
                " department.<br>"+
                "Thank you for your patience.<br>"+
                company.endTag();

        emailService.sendEmail(raiserMessage, raiserSubject, ticket.getRaisedBy().getUsername(), null);

    }



    @Override
    public void assignTo(Ticket ticket) throws OperationNotSupportedException{

        /*check if the user is an employee*/
        if (!userManagementService.checkUserByUsernameAndAuthority(
                ticket.getAssignedTo().getUsername(), userAuthorityUtility.getFor("employee")))
            throw new OperationNotSupportedException();

        /*assign the ticket*/
        Ticket updatedTicket = ticketManagementService.updateTicket(ticket);

        /*send notification*/
        sendNotification(updatedTicket);
    }

}
