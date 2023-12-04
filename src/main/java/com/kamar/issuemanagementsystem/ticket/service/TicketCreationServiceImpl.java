package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.external_resouces.data.AttachmentResourceDto;
import com.kamar.issuemanagementsystem.external_resouces.service.EmailService;
import com.kamar.issuemanagementsystem.ticket.controller.TicketManagementController;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.repository.TicketRepository;
import com.kamar.issuemanagementsystem.ticket.utility.util.TicketUtilities;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * the implementation of the ticket creation service.
 * @author kamar baraka.*/


@Service
@RequiredArgsConstructor
@Transactional
public class TicketCreationServiceImpl implements TicketCreationService {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final CompanyProperties company;
    private final TicketUtilities ticketUtilities;
    private final UserAuthorityUtility userAuthorityUtility;

    private void sendCreationNotification(Ticket ticket){

        /*the get ticket link*/
        String linkToTicket = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                TicketManagementController.class).getTicketById(
                        ticket.getTicketNumber())).withRel("ticket").getHref();
        /*the subject*/
        String subject = "Ticket Raised";
        /*construct the message for the admin*/
        String message = "<div >"+ ticket.getRaisedBy().getUsername()+
                " raised a ticket \"" + ticket.getTicketNumber()+
                " "+ ticket.getTitle()+ "\" to <h3 style='color: blue;' >"+ ticket.getDepartmentAssigned().getDepartmentName()+
                " </h3> department. </div>"+
                " <br> "+
                " <h3><a href=\""+ linkToTicket+ "\" >Ticket</a></h3> <br>"+
                "Thank you, <br>"+
                company.endTag();

        /*get attachments*/
        List<AttachmentResourceDto> attachments = ticketUtilities.getTicketAttachments(ticket);

        /*get all admins and send the admin notification email*/
        List<User> admins = userRepository.findUserByAuthoritiesContaining(userAuthorityUtility.getFor("admin"));

        if (!admins.isEmpty()) {

            admins.forEach(admin -> emailService.sendEmail(
                    message, subject, admin.getUsername(), attachments
            ));
        }

        /*notify the department*/
        emailService.sendEmail(message, subject, ticket.getDepartmentAssigned().getDepartmentEmail(), attachments);

        /*send notification to the raiser*/
        String raiserSubject = "Ticket Success";
        String raiserMessage = "<div > Dear "+ ticket.getRaisedBy().getUsername()+
                ", thank you for raising your issue. <br>"+
                "It will be handled within a week. </div><br>"+ company.endTag();

        emailService.sendEmail(raiserMessage, raiserSubject, ticket.getRaisedBy().getUsername(), null);

    }

    @Override
    public Ticket createTicket(Ticket ticket) {

        /*persist the ticket*/
        Ticket savedTicket = ticketRepository.save(ticket);
        /*send notification*/
        sendCreationNotification(savedTicket);

        return savedTicket;
    }
}
