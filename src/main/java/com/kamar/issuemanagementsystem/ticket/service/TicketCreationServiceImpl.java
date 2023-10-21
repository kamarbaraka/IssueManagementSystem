package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.external_resouces.EmailService;
import com.kamar.issuemanagementsystem.ticket.controller.TicketCreationController;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.repository.TicketRepository;
import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * the implementation of the ticket creation service.
 * @author kamar baraka.*/


@Service
@RequiredArgsConstructor
public class TicketCreationServiceImpl implements TicketCreationService {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;

    private void sendCreationNotification(Ticket ticket){

        /*the get ticket link*/
        String linkToTicket = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                TicketCreationController.class).getTicketById(
                        ticket.getTicketId())).toUriComponentsBuilder().toUriString();
        /*the subject*/
        String subject = "Ticket Raised";
        /*construct the message*/
        String message = ticket.getRaisedBy() + " raised a ticket \"" + ticket.getTitle() + "\". #" + ticket.getTicketId()+
                "\n "+ linkToTicket;
        /*get all admins*/
        userRepository.findUsersByAuthorityOrderByCreatedOn(Authority.ADMIN).ifPresentOrElse(
                admins ->
                    /*send mails to each admin*/
                    admins.parallelStream().forEach(
                            admin -> emailService.sendEmail(message, subject, admin.getUsername())
                    ),
                () -> {}
        );
    }

    @Transactional
    @Override
    public Ticket createTicket(Ticket ticket) {

        /*persist the ticket*/
        Ticket savedTicket = ticketRepository.save(ticket);
        /*send notification*/
        sendCreationNotification(savedTicket);

        return savedTicket;
    }
}
