package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.external_resouces.EmailService;
import com.kamar.issuemanagementsystem.rating.data.dto.UserRatingDTO;
import com.kamar.issuemanagementsystem.rating.exceptions.RatingException;
import com.kamar.issuemanagementsystem.rating.service.RatingService;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketUserFeedbackDTO;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketFeedbackException;
import com.kamar.issuemanagementsystem.ticket.repository.TicketRepository;
import com.kamar.issuemanagementsystem.user.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 * implementation of the ticket feedback service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class TicketFeedbackServiceImpl implements TicketFeedbackService {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;
    private final TicketManagementService ticketManagementService;
    private final UserManagementService userManagementService;
    private final RatingService ratingService;

    private void unsatisfiedNotification(final TicketUserFeedbackDTO userFeedbackDTO, final Ticket ticket){

        /*compose the email*/
        String subject = "Submission Feedback";
        String message = "Ticket #" + ticket.getTicketId() + " " + ticket.getTitle() +
                " needs more the following resolution; \n" + userFeedbackDTO.feedback() + ". Please resolve in due time.";

        /*send the email*/
        emailService.sendEmail(message, subject, ticket.getAssignedTo().getUsername());
    }
    private void satisfactionNotification(final TicketUserFeedbackDTO userFeedbackDTO, final Ticket ticket){

        /*compose the email*/
        String subject = "Congratulation!";
        String message = "Congratulation!, you have managed to resolve ticket #" + ticket.getTicketId() +
                " " + ticket.getTitle() + ". Your rating is " + userFeedbackDTO.serviceRating();

        /*send the email*/
        emailService.sendEmail(message, subject, ticket.getAssignedTo().getUsername());
    }

    @Override
    public void sendFeedback(final TicketUserFeedbackDTO userFeedbackDTO, final long ticketId,
                             @AuthenticationPrincipal UserDetails authenticatedUser) throws TicketFeedbackException {

        /*get the ticket*/
        Ticket ticket = ticketManagementService.getTicketById(ticketId);

        /*check for identity*/
        if (!ticket.getRaisedBy().getUsername().equals(authenticatedUser.getUsername()))
            throw new TicketFeedbackException("you dont own the ticket");

        /*check for satisfaction*/
        if (!userFeedbackDTO.satisfied()){
            /*notify the assignee*/
            unsatisfiedNotification(userFeedbackDTO, ticket);
            /*update the status*/
            ticket.setStatus(TicketStatus.ASSIGNED);
            ticketRepository.save(ticket);

        }

        /*update the status and rating*/
        ticket.setStatus(TicketStatus.CLOSED);
        try {
            ratingService.rateUser(new UserRatingDTO(
                    ticket.getAssignedTo().getUsername(),
                    userFeedbackDTO.serviceRating()));
        } catch (RatingException e) {
            throw new TicketFeedbackException(e.getMessage());
        }
        ticketRepository.save(ticket);

        /*notify*/
        satisfactionNotification(userFeedbackDTO, ticket);
    }
}
