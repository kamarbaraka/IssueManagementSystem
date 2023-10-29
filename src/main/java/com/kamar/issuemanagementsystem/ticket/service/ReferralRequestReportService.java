package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;

import java.util.List;

/**
 * the referral request reports.
 * @author kamar baraka.*/

public interface ReferralRequestReportService {

    List<ReferralRequest> getReferralRequestsTo(String to);
    List<ReferralRequest> getReferralRequestsFrom(String from);
    List<ReferralRequest> getRejectedRequestsFrom(String from);
    List<ReferralRequest> getAcceptedRequestsFrom(String from);
}
