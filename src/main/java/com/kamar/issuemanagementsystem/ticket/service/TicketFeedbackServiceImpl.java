package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.attachment.exception.AttachmentException;
import com.kamar.issuemanagementsystem.attachment.utils.AttachmentMapper;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.external_resouces.data.AttachmentResourceDto;
import com.kamar.issuemanagementsystem.external_resouces.service.EmailService;
import com.kamar.issuemanagementsystem.rating.data.dto.UserRatingDTO;
import com.kamar.issuemanagementsystem.rating.exceptions.RatingException;
import com.kamar.issuemanagementsystem.rating.service.RatingService;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketUserFeedbackDTO;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketException;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketFeedbackException;
import com.kamar.issuemanagementsystem.ticket.repository.ReferralRequestRepository;
import com.kamar.issuemanagementsystem.ticket.repository.TicketRepository;
import com.kamar.issuemanagementsystem.ticket.utility.util.TicketUtilities;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * implementation of the ticket feedback service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Log4j2
@Transactional
public class TicketFeedbackServiceImpl implements TicketFeedbackService {

    private final TicketRepository ticketRepository;
    private final EmailService emailService;
    private final TicketManagementService ticketManagementService;
    private final RatingService ratingService;
    private final DepartmentRepository departmentRepository;
    private final CompanyProperties company;
    private final TicketUtilities ticketUtilities;
    private final ReferralRequestRepository referralRequestRepository;
    private final AttachmentMapper attachmentMapper;

    private void unsatisfiedNotification(final TicketUserFeedbackDTO userFeedbackDTO, final Ticket ticket){

        /*compose the email*/
        String subject = "Submission Feedback";
        String message = "Ticket \""+ ticket.getTicketNumber()+ " "+ ticket.getTitle()+ "\" needs more attention: <br>"+
                "<div style='border-radius: 20px; background: grey;'>"+
                "<p style='color: yellow;'>"+
                userFeedbackDTO.feedback()+
                "</p><div><br>"+
                "Please resolve it in due time.<br>"+ company.endTag();

        /*compose the email for the user*/
        String userSubject = "Feedback";
        String userMessage = "Thank you for submitting your feedback. We well get back to you soon.<br>"+
                company.endTag();

        /*get attachments*/
        List<AttachmentResourceDto> attachments = ticketUtilities.getTicketAttachments(ticket);

        /*send the email*/
        emailService.sendEmail(message, subject, ticket.getAssignedTo().getUsername(), attachments);
        emailService.sendEmail(userMessage, userSubject, ticket.getRaisedBy().getUsername(), null);
    }
    private void satisfactionNotification(final TicketUserFeedbackDTO userFeedbackDTO, final Ticket ticket){

        /*compose the email*/
        String subject = "Congratulation!";
        String message = "Congratulation!, you have managed to resolve ticket \""+ ticket.getTicketNumber()+
                " "+ ticket.getTitle()+ "\". Your rating is "+ userFeedbackDTO.serviceRating()+ "<br>"+
                company.endTag();

        /*compose the email for the user*/
        String userSubject = "Feedback";
        String userMessage = "Thank you for submitting your feedback. We are glad to be of help to you 😊.<br>"+
                company.endTag();

        /*send the email*/
        emailService.sendEmail(message, subject, ticket.getAssignedTo().getUsername(), null);
        emailService.sendEmail(userMessage, userSubject, ticket.getRaisedBy().getUsername(), null);
    }

    @Override
    public void sendFeedback( TicketUserFeedbackDTO feedbackDTO)
            throws TicketFeedbackException, TicketException {

        /*get authenticated user*/
        UserDetails authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        /*get the ticket*/
        Ticket ticket = ticketManagementService.getTicketById(feedbackDTO.ticketNumber());

        /*check if ticket is submitted to send feedback*/
        if (!ticket.getStatus().equals(TicketStatus.SUBMITTED)) {
            /*throw*/
            throw new TicketFeedbackException("the ticket is not submitted yet");
        }

        /*check for identity*/
        if (!ticket.getRaisedBy().getUsername().equals(authenticatedUser.getUsername()))
            throw new TicketFeedbackException("you dont own the ticket");

        /*check for satisfaction*/
        if (!feedbackDTO.satisfied()){
            /*notify the assignee*/
            unsatisfiedNotification(feedbackDTO, ticket);
            /*update the status*/
            ticket.setStatus(TicketStatus.ASSIGNED);

            /*create the attachments*/
            List<MultipartFile> multipartFiles = feedbackDTO.attachments();
            /*check if there are attachments*/
            if (!multipartFiles.isEmpty()) {
                List<Attachment> attachments = multipartFiles.stream().map(multipartFile -> {
                    try {
                        return attachmentMapper.multipartToAttachment(multipartFile);
                    } catch (AttachmentException e) {
                        /*log*/
                        log.warn(e.getMessage());
                    }
                    return null;
                }).toList();
                /*add the attachments to the ticket*/
                ticket.getAttachments().addAll(attachments);
            }
            ticketRepository.save(ticket);

            return;

        }

        /*check if the ticket is linked to a referral request*/
        referralRequestRepository.findReferralRequestsByRefferedTicket(ticket).ifPresentOrElse(
                referralRequest -> {referralRequestRepository.deleteById(referralRequest.getRequestId());},
                () -> {}
        );

        /*update the status and rating*/
        ticket.setStatus(TicketStatus.CLOSED);
        try {
            /*rate the user*/
            ratingService.rateUser(new UserRatingDTO(
                    ticket.getAssignedTo().getUsername(),
                    feedbackDTO.serviceRating()));
            /*rate the department*/
            Department department = departmentRepository.findDepartmentByMembersContaining(ticket.getAssignedTo()).orElseThrow(
                    () -> new TicketFeedbackException("no department to rate")
            );
            ratingService.rateDepartment(department);
        } catch (RatingException e) {
            throw new TicketFeedbackException(e.getMessage());
        }
        ticketRepository.save(ticket);

        /*notify*/
        satisfactionNotification(feedbackDTO, ticket);
    }
}
