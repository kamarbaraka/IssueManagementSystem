package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.repository.UserAuthorityRepository;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.external_resouces.data.AttachmentResourceDto;
import com.kamar.issuemanagementsystem.external_resouces.service.EmailService;
import com.kamar.issuemanagementsystem.ticket.controller.ReferralRequestController;
import com.kamar.issuemanagementsystem.ticket.controller.TicketManagementController;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.data.dto.MembersDto;
import com.kamar.issuemanagementsystem.ticket.data.dto.ReferralRequestDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketReferralDTO;
import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.exceptions.ReferralRequestException;
import com.kamar.issuemanagementsystem.ticket.repository.ReferralRequestRepository;
import com.kamar.issuemanagementsystem.ticket.repository.TicketRepository;
import com.kamar.issuemanagementsystem.ticket.utility.mapper.ReferralRequestMapper;
import com.kamar.issuemanagementsystem.ticket.utility.util.TicketUtilities;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import com.kamar.issuemanagementsystem.user.utility.util.UserUtilityService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
@Transactional
public class ReferralRequestManagementServiceImpl implements ReferralRequestManagementService {

    private final ReferralRequestRepository referralRequestRepository;
    private final EmailService emailService;
    private final UserRepository userRepository;
    private final TicketRepository ticketRepository;
    private final ReferralRequestMapper referralRequestMapper;
    private final CompanyProperties company;
    private final TicketUtilities ticketUtilities;
    private final UserUtilityService userUtilityService;
    private final DepartmentRepository departmentRepository;

    private void sendReferralRequest(final ReferralRequest referralRequest){


        /*add links*/
        Link acceptRequest = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                ReferralRequestController.class).respondToReferralRequest(
                        true, referralRequest.getRequestId())).withRel("accept");

        Link rejectRequest = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(ReferralRequestController.class)
                        .respondToReferralRequest(false, referralRequest.getRequestId()))
                .withRel("reject");

        /*compose the email*/
        String subject = "Referral Request";
        String message = referralRequest.getFrom().getUsername()+ " has requested you to handle the ticket #" +
                referralRequest.getRefferedTicket().getTicketNumber()+ " \""+ referralRequest.getRefferedTicket().getTitle()
                + "\" <br>"+
                "<h4 style=\"color: green;\"><a href=\""+ acceptRequest.getHref()+ "\">Accept</a></h4> <br>"+
                "<h4 style=\"color: red;\"><a href=\""+ rejectRequest.getHref()+ "\">Reject</a></h4> <br>"+
                company.endTag();

        /*send the email*/
        emailService.sendEmail(message, subject, referralRequest.getTo().getUsername(), null);
    }

    private void receiveAcceptedRequestNotification(Ticket ticket){

        /*set the message and subject*/
        String subject = "Reassigned";

        Link ticketLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TicketManagementController.class)
                        .getTicketById(ticket.getTicketNumber())).withRel("ticket");

        String message = "The ticket #" + ticket.getTicketNumber() + " " + ticket.getTitle()
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
                + referralRequest.getRefferedTicket().getTicketNumber() + " "
                + referralRequest.getRefferedTicket().getTitle() + "\". <br>"+
                company.endTag();

        /*send message*/
        emailService.sendEmail(message, subject, referralRequest.getFrom().getUsername(), null);
    }

    private void sendRejectedRequestNotification(ReferralRequest referralRequest){

        /*set the email*/
        String subject = "Request Rejected";
        String message = referralRequest.getTo().getUsername() + " rejected your referral request for ticket \"#"
                + referralRequest.getRefferedTicket().getTicketNumber() + " "
                + referralRequest.getRefferedTicket().getTitle()+ "\". <br>"+
                company.endTag();

        /*send notification*/
        emailService.sendEmail(message, subject, referralRequest.getFrom().getUsername(), null);
    }

    private ReferralRequest createAReferralRequest(ReferralRequest referralRequest){

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
    public ReferralRequestDTO referTicketTo(TicketReferralDTO requestDTO)
            throws ReferralRequestException {

        /*get the authenticated user*/
        UserDetails authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        /*get data*/
        Ticket ticket = ticketRepository.findById(requestDTO.ticketToRefer()).orElseThrow(
                () -> new ReferralRequestException("no such ticket to refer.")
        );
        User fromUser = ticket.getAssignedTo();
        User toUser = userRepository.findUserByUsername(requestDTO.to()).orElseThrow(
                () -> new ReferralRequestException("no such employee to refer to")
        );
        Department toUserDepartment = departmentRepository.findDepartmentByMembersContaining(toUser).orElseThrow(
                () -> new ReferralRequestException("user doesn't belong to a department."));

        /*filter for employee */
        if (userUtilityService.hasAuthority(authenticatedUser, "employee")) {

            /*check if he owns the ticket*/
            if (!ticket.getAssignedTo().getUsername().equals(authenticatedUser.getUsername())) {
                throw new ReferralRequestException("you dont own the resource");
            }
            /*check if user to refer to belongs to the department*/
            if (!ticket.getDepartmentAssigned().equals(toUserDepartment)) {
                throw new ReferralRequestException("can't refer to a different department");
            }
        }

        /*filter for department admin*/
        if (userUtilityService.hasAuthority(authenticatedUser, "department_admin")) {

            /*check if the ticket belongs to his department*/
            User user = (User) authenticatedUser;
            Department authenticatedUserDept = departmentRepository.findDepartmentByMembersContaining(user).orElseThrow(
                    () -> new ReferralRequestException("you dont belong to a department."));
            if (!ticket.getDepartmentAssigned().equals(authenticatedUserDept)) {
                throw new ReferralRequestException("you dont own the resource");
            }
        }

        /*check if the ticket is assigned to be eligible for referral*/
        if (!ticket.getStatus().equals(TicketStatus.ASSIGNED)) {
            /*throw*/
            throw new ReferralRequestException("ticket is not assigned for it to be referred.");
        }

        /*check if the ticket is linked to a referral request*/
        referralRequestRepository.findReferralRequestsByRefferedTicket(ticket).ifPresent(
                referralRequest -> referralRequestRepository.deleteById(referralRequest.getRequestId())
        );

        /*check that the employee is not referring to himself*/
        if (authenticatedUser.getUsername().equals(requestDTO.to())) {
            /*throw*/
            throw new ReferralRequestException("you can't refer the ticket to yourself");
        }

        /*construct a referral request*/
        ReferralRequest referralRequest = new ReferralRequest();
        referralRequest.setRefferedTicket(ticket);
        referralRequest.setFrom(fromUser);
        referralRequest.setTo(toUser);
        referralRequest.setReason(requestDTO.reason());

        /*send a referral request*/
        ReferralRequest aReferralRequest = createAReferralRequest(referralRequest);
        /*map the request and return*/
        return referralRequestMapper.entityToDTO(aReferralRequest);


    }

    @Override
    public void respondToReferralRequest(long referralRequestId, boolean response) throws ReferralRequestException {

        /*get the employee*/
        String loggedInUsername = SecurityContextHolder.getContext().getAuthentication().getName();
        User loggedInUser = userRepository.findUserByUsername(loggedInUsername).orElseThrow(
                () -> new ReferralRequestException("oops! user dont exist")
        );

        /*get the referral request*/
        ReferralRequest referralRequest = referralRequestRepository.findById(referralRequestId).orElseThrow(
                () -> new ReferralRequestException("referral req not found")
        );

        /*check if the referral request belongs to the user*/
        if (!referralRequest.getTo().equals(loggedInUser)) {

            /*throw*/
            throw new ReferralRequestException("you don't own the req");
        }


        if (response){

            /*get the referred ticket and update the assignment*/
            Ticket refferedTicket = referralRequest.getRefferedTicket();
            refferedTicket.setAssignedTo(referralRequest.getTo());

            ticketRepository.save(refferedTicket);

            /*delete the referral request*/
            referralRequestRepository.deleteById(referralRequest.getRequestId());

            /*notify the referrer*/
            sendAcceptedRequestNotification(referralRequest);
            receiveAcceptedRequestNotification(refferedTicket);

            return;
        }

        /*notify the referrer*/
        referralRequestRepository.deleteById(referralRequest.getRequestId());
        sendRejectedRequestNotification(referralRequest);

    }

    @Override
    public MembersDto refer() throws ReferralRequestException {

        /*get authenticated user*/
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = (User) userDetails;

        /*get department*/
        Department department = departmentRepository.findDepartmentByMembersContaining(user).orElseThrow(
                () -> new ReferralRequestException("user doesn't belong to a department"));

        List<String> members = department.getMembers().stream().map(User::getUsername).toList();

        return new MembersDto(department.getDepartmentName(), members);
    }
}
