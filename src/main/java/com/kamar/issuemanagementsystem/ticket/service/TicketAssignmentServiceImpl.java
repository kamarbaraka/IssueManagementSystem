package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.external_resouces.data.AttachmentResourceDto;
import com.kamar.issuemanagementsystem.external_resouces.service.EmailService;
import com.kamar.issuemanagementsystem.ticket.controller.TicketManagementController;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.utility.util.TicketUtilities;
import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.naming.OperationNotSupportedException;
import java.util.List;

/**
 * implementation of the ticket assignment service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class TicketAssignmentServiceImpl implements TicketAssignmentService {

    private final UserManagementService userManagementService;
    private final TicketManagementService ticketManagementService;
    private final EmailService emailService;
    private final DepartmentRepository departmentRepository;
    private final CompanyProperties company;
    private final TicketUtilities ticketUtilities;


    private UserDetails getAuthenticatedUser(){

        return  (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
    private void sendNotification(Ticket ticket){

        /*set the subject and Message*/
        String subject = "Ticket assignment";
        Link ticketLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                TicketManagementController.class).getTicketById(ticket.getTicketId(),
                getAuthenticatedUser())).withRel("the ticket");

        String message = "You have been assigned the ticket #" + ticket.getTicketId()+ " "+ ticket.getTitle() +
                ". Resolve it before "+ "<h4 style=\"color: red;\">"+ ticket.getDeadline()+ "</h4> <br>"+
                "  <h4><a href=\""+ ticketLink.getHref()+ "\" >Ticket</a></h4> <br>"+
                company.endTag();

        /*get attachments*/
        List<AttachmentResourceDto> attachments = ticketUtilities.getTicketAttachments(ticket);

        /*send the message*/
        emailService.sendEmail(message, subject, ticket.getAssignedTo().getUsername(), attachments);

        /*compose and send notification to the raiser*/
        String raiserSubject = "Ticket handling";
        String raiserMessage = "Dear "+ ticket.getRaisedBy().getUsername()+ ",Your ticket #" + ticket.getTicketId()
                + " \"" + ticket.getTitle() + "\""+
                " is being handled by the " + departmentRepository.
                findDepartmentByMembersContaining(ticket.getAssignedTo()).orElseThrow().getDepartmentName()+
                " department.<br>"+
                "Thank you for your patience.<br>"+
                company.endTag();

        emailService.sendEmail(raiserMessage, raiserSubject, ticket.getRaisedBy().getUsername(), null);

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
