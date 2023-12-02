package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;
import com.kamar.issuemanagementsystem.ticket.repository.ReferralRequestRepository;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.service.UserManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * implementation of the referral request reports.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Transactional
public class ReferralRequestReportServiceImpl implements ReferralRequestReportService {

    private final ReferralRequestRepository referralRequestRepository;
    private final UserManagementService userManagementService;

    @Override
    public List<ReferralRequest> getReferralRequestsTo(final String to) {

        /*get the user requested*/
        User user = userManagementService.getUserByUsername(to);
        /*get the referrals*/
        return referralRequestRepository.findReferralRequestsByToOrderByRequestedOn(user);
    }

    @Override
    public List<ReferralRequest> getReferralRequestsFrom(final String from) {

        /*get the user who requested*/
        User user = userManagementService.getUserByUsername(from);
        /*get the referrals*/
        return referralRequestRepository.findReferralRequestsByFromOrderByRequestedOn(user);
    }

    @Override
    public List<ReferralRequest> getRejectedRequestsFrom(final String from) {

        /*get the user who requested*/
        User user = userManagementService.getUserByUsername(from);
        /*get the rejected referrals*/
        return referralRequestRepository.findReferralRequestsByFromAndAcceptedOrderByRequestedOn(user, false);
    }

    @Override
    public List<ReferralRequest> getAcceptedRequestsFrom(String from) {

        /*get the user*/
        User user = userManagementService.getUserByUsername(from);
        /*get the referrals*/
        return referralRequestRepository.findReferralRequestsByToAndAcceptedOrderByRequestedOn(user, true);
    }
}
