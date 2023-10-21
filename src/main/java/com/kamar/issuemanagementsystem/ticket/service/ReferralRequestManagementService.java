package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;

/**
 * the referral management service.
 * @author kamar baraka.*/

public interface ReferralRequestManagementService {

    ReferralRequest createAReferralRequest(ReferralRequest referralRequest);

    void acceptReferralRequestById(long id);

    void rejectReferralRequestById(long id);

    ReferralRequest getReferralRequestById(long id);
}
