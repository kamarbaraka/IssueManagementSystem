package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.repository.UserAuthorityRepository;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.external_resouces.data.AttachmentResourceDto;
import com.kamar.issuemanagementsystem.external_resouces.service.EmailService;
import com.kamar.issuemanagementsystem.ticket.controller.ReferralRequestController;
import com.kamar.issuemanagementsystem.ticket.controller.TicketManagementController;
import com.kamar.issuemanagementsystem.ticket.data.dto.ReferralRequestDTO;
import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.exceptions.ReferralRequestException;
import com.kamar.issuemanagementsystem.ticket.repository.ReferralRequestRepository;
import com.kamar.issuemanagementsystem.ticket.repository.TicketRepository;
import com.kamar.issuemanagementsystem.ticket.utility.mapper.ReferralRequestMapper;
import com.kamar.issuemanagementsystem.ticket.utility.util.TicketUtilities;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * implementation of the referral management service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Transactional
public class ReferralRequestManagementServiceImpl implements ReferralRequestManagementService {

    private final ReferralRequestRepository referralRequestRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final ReferralRequestMapper referralRequestMapper;
    private final CompanyProperties company;
    private final TicketUtilities ticketUtilities;
    private final UserAuthorityUtility userAuthorityUtility;

    private void sendReferralRequest(final ReferralRequest referralRequest){


        /*authenticated user*/
        UserDetails authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        /*add links*/
        Link acceptRequest = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                ReferralRequestController.class).respondToReferralRequest(
                        true, referralRequest.getRequestId(), authenticatedUser)).withRel("accept");

        Link rejectRequest = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ReferralRequestController.class)
                        .respondToReferralRequest(false, referralRequest.getRequestId(), authenticatedUser))
                .withRel("reject");

        /*compose the email*/
        String subject = "Referral Request";
        String message = referralRequest.getFrom().getUsername()+ " has requested you to handle the ticket #" +
                referralRequest.getRefferedTicket().getTicketId()+ " \""+ referralRequest.getRefferedTicket().getTitle()
                + "\" <br>"+
                "<h4 style=\"color: green;\"><a href=\""+ acceptRequest.getHref()+ "\">Accept</a></h4> <br>"+
                "<h4 style=\"color: red;\"><a href=\""+ rejectRequest.getHref()+ "\">Reject</a></h4> <br>"+
                company.endTag();

        /*send the email*/
        emailService.sendEmail(message, subject, referralRequest.getTo().getUsername(), null);
    }

    private void receiveAcceptedRequestNotification(Ticket ticket, UserDetails authenticatedUser){

        /*set the message and subject*/
        String subject = "Reassigned";

        Link ticketLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TicketManagementController.class)
                        .getTicketById(ticket.getTicketId(), authenticatedUser)).withRel("ticket");

        String message = "The ticket #" + ticket.getTicketId() + " " + ticket.getTitle()
                + " is assigned to you upon accepting the referral request. Resolve it before "+
                "<h4 style='color: red;'>" + ticket.getDeadline()+ "</h4> <br>"+
                "<h4><a href=\""+ ticketLink.getHref()+ "\">Ticket</a></h4> <br>"+
                company.endTag();

        /*get attachments*/
        List<AttachmentResourceDto> attachments = ticketUtilities.getTicketAttachments(ticket);

        /*send the message*/
        emailService.sendEmail(message, subject, ticket.getAssignedTo().getUsername(), attachments);


    }

    private void sendAcceptedRequestNotification(ReferralRequest referralRequest){

        /*set the email*/
        String subject = "Request Accepted";

        String message = referralRequest.getTo().getUsername() + " accepted your referral request for ticket \"#"
                + referralRequest.getRefferedTicket().getTicketId() + " "
                + referralRequest.getRefferedTicket().getTitle() + "\". <br>"+
                company.endTag();

        /*send message*/
        emailService.sendEmail(message, subject, referralRequest.getFrom().getUsername(), null);
    }

    private void sendRejectedRequestNotification(ReferralRequest referralRequest){

        /*set the email*/
        String subject = "Request Rejected";
        String message = referralRequest.getTo().getUsername() + " rejected your referral request for ticket \"#"
                + referralRequest.getRefferedTicket().getTicketId() + " "
                + referralRequest.getRefferedTicket().getTitle()+ "\". <br>"+
                company.endTag();

        /*send notification*/
        emailService.sendEmail(message, subject, referralRequest.getFrom().getUsername(), null);
    }

    private ReferralRequest createAReferralRequest(ReferralRequest referralRequest)
            throws ReferralRequestException{

        /*check whether the referred to is an employee*/
        if (!referralRequest.getTo().getAuthorities().contains(userAuthorityUtility.getFor("employee")))
            throw new ReferralRequestException("user is not an employee");
        /*create a referral request*/
        ReferralRequest savedRequest = referralRequestRepository.save(referralRequest);
        /*notify*/
        this.sendReferralRequest(referralRequest);
        /*return the referral*/
        return savedRequest;
    }

    @Override
    public ReferralRequest getReferralRequestById(long id) throws ReferralRequestException {

        /*get referral request*/
        return referralRequestRepository.findById(id).orElseThrow(
                () -> new ReferralRequestException("referral request not found")
        );
    }

    @Override
    public ReferralRequestDTO referTicketTo(Ticket ticket, String to)
            throws ReferralRequestException {

        /*get data*/
        User fromUser = ticket.getAssignedTo();
        User toUser = userRepository.findUserByUsername(to).orElseThrow(
                () -> new ReferralRequestException(" employee to refer to not found")
        );

        /*construct a referral request*/
        ReferralRequest referralRequest = new ReferralRequest();
        referralRequest.setRefferedTicket(ticket);
        referralRequest.setFrom(fromUser);
        referralRequest.setTo(toUser);

        /*send a referral request*/
        ReferralRequest aReferralRequest = createAReferralRequest(referralRequest);
        /*map the request and return*/
        return referralRequestMapper.entityToDTO(aReferralRequest);


    }

    @Override
    @Transactional
    public void respondToReferralRequest(long referralRequestId, boolean response,
                                         UserDetails authenticatedUser) throws ReferralRequestException {

        /*get the referral request*/
        ReferralRequest referralRequest = referralRequestRepository.findById(referralRequestId).orElseThrow(
                () -> new ReferralRequestException("referral req not found")
        );

        if (!response){

            /*notify the referrer*/
            referralRequestRepository.deleteById(referralRequest.getRequestId());
            sendRejectedRequestNotification(referralRequest);
        }

        /*get the referred ticket and update the assignment*/
        Ticket refferedTicket = referralRequest.getRefferedTicket();
        refferedTicket.setAssignedTo(referralRequest.getTo());
        ticketRepository.save(refferedTicket);

        /*notify the referrer*/
        sendAcceptedRequestNotification(referralRequest);
        receiveAcceptedRequestNotification(refferedTicket, authenticatedUser);
    }
}
