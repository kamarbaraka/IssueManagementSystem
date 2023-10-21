package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;
import com.kamar.issuemanagementsystem.ticket.repository.ReferralRequestRepository;
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

    @Override
    public ReferralRequest createAReferralRequest(ReferralRequest referralRequest) {

        /*create a referral request*/
        return referralRequestRepository.save(referralRequest);
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
