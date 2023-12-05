package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.external_resouces.data.AttachmentResourceDto;
import com.kamar.issuemanagementsystem.external_resouces.service.EmailService;
import com.kamar.issuemanagementsystem.ticket.controller.TicketManagementController;
import com.kamar.issuemanagementsystem.ticket.data.TicketPriority;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketAssignmentDTO;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.repository.TicketRepository;
import com.kamar.issuemanagementsystem.ticket.utility.util.TicketUtilities;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import com.kamar.issuemanagementsystem.user.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
    private final TicketRepository ticketRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;


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
    public void assignTo(TicketAssignmentDTO assignmentDTO) throws OperationNotSupportedException{


        /*check that the user to assign exists*/
        User userToAssign = userRepository.findUserByUsername(assignmentDTO.assignTo()).orElseThrow(
                () -> new OperationNotSupportedException("no such user to assign")
        );

        /*check if the user to assign is an employee*/
        if (!userManagementService.checkUserByUsernameAndAuthority(
                assignmentDTO.assignTo(), userAuthorityUtility.getFor("employee")))
            throw new OperationNotSupportedException("the user to assign is not an employee.");

        /*check if the ticket exists*/
        Ticket ticket = ticketRepository.findById(assignmentDTO.ticketNumber()).orElseThrow(
                () -> new OperationNotSupportedException("no such ticket to be assigned.")
        );

        /*check if the ticket is open or assigned*/
        if (ticket.getStatus().equals(TicketStatus.SUBMITTED) || ticket.getStatus().equals(TicketStatus.CLOSED)) {
            /*throw*/
            throw new OperationNotSupportedException("the ticket is already submitted or closed");
        }

        /*check that the user does not assign to the same person*/
        if (userToAssign.equals(ticket.getAssignedTo())) {
            /*throw*/
            throw new OperationNotSupportedException("you cant assign to the same person");
        }

        /*confirm that the ticket belongs to the same department as the user to assign*/
        Department ticketDepartment = ticket.getDepartmentAssigned();
        Department userToAssignDept = departmentRepository.findDepartmentByMembersContaining(userToAssign).orElseThrow(
                () -> new OperationNotSupportedException("user to assign does not belong to a department")
        );

        if (!userToAssignDept.equals(ticketDepartment)) {
            /*throw*/
            throw new OperationNotSupportedException("user to assign does not belong to the same department as the ticket");
        }

        /*get the priority and deadline*/
        TicketPriority priority = TicketPriority.valueOf(assignmentDTO.priority().toUpperCase());
        LocalDate deadline;
        try {
            deadline = LocalDate.parse(assignmentDTO.deadline(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } catch (Exception e) {
            throw new OperationNotSupportedException("deadline format not supported");
        }

        /*set the ticket*/
        ticket.setAssignedTo(userToAssign);
        ticket.setPriority(priority);
        ticket.setDeadline(deadline);

        if (!ticket.getStatus().equals(TicketStatus.ASSIGNED)) {
            /*set the status*/
            ticket.setStatus(TicketStatus.ASSIGNED);
        }

        /*assign the ticket*/
        Ticket updatedTicket = ticketRepository.save(ticket);

        /*send notification*/
        sendNotification(updatedTicket);
    }

}
