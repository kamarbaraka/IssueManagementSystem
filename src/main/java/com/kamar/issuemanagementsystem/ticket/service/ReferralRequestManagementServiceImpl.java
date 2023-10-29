package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.external_resouces.EmailService;
import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;
import com.kamar.issuemanagementsystem.ticket.exceptions.ReferralRequestException;
import com.kamar.issuemanagementsystem.ticket.repository.ReferralRequestRepository;
import com.kamar.issuemanagementsystem.user.data.Authority;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * implementation of the referral management service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class ReferralRequestManagementServiceImpl implements ReferralRequestManagementService {

    private final ReferralRequestRepository referralRequestRepository;
    private final TicketAssignmentService ticketAssignmentService;
    private final EmailService emailService;

    private void notifyReferralRequest( final ReferralRequest referralRequest){

        /*compose the email*/
        String subject = "Referral Request";
        String message = referralRequest.getFrom().getUsername() + " requested you to handle the ticket #" +
                referralRequest.getRefferedTicket().getTicketId() + referralRequest.getRefferedTicket().getTitle();

        /*send the email*/
        emailService.sendEmail(message, subject, referralRequest.getTo().getUsername());
    }

    @Override
    public ReferralRequest createAReferralRequest(ReferralRequest referralRequest) throws ReferralRequestException{

        /*check whether the referred to is an employee*/
        if (!referralRequest.getTo().getAuthorities().contains(Authority.EMPLOYEE))
            throw new ReferralRequestException("user if not an employee");
        /*create a referral request*/
        ReferralRequest savedRequest = referralRequestRepository.save(referralRequest);
        /*notify*/
        this.notifyReferralRequest(referralRequest);
        /*return the referral*/
        return savedRequest;
    }

    @Override
    public void acceptReferralRequestById(long id) {

        /*get referral request*/
        ReferralRequest referral = getReferralRequestById(id);
        /*accept*/
        referral.setAccepted(true);

        /*notify*/
        ticketAssignmentService.respondToReferralRequest(referral, true);

    }

    @Override
    public void rejectReferralRequestById(long id) {

        /*check if exists*/
        referralRequestRepository.findById(id).ifPresentOrElse(
                referralRequest -> {

                    /*reject hence delete*/
                    referralRequestRepository.deleteById(referralRequest.getRequestId());

                    /*notify*/
                    ticketAssignmentService.respondToReferralRequest(referralRequest, false);
                },
                () -> {}
        );

    }

    @Override
    public ReferralRequest getReferralRequestById(long id) {

        /*get referral request*/
        return referralRequestRepository.findById(id).orElseThrow();
    }
}
