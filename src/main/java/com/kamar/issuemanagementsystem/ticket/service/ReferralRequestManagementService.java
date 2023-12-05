package com.kamar.issuemanagementsystem.ticket.service;

import com.kamar.issuemanagementsystem.ticket.data.dto.MembersDto;
import com.kamar.issuemanagementsystem.ticket.data.dto.ReferralRequestDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketReferralDTO;
import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.exceptions.ReferralRequestException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;

/**
 * the referral management service.
 * @author kamar baraka.*/

public interface ReferralRequestManagementService {

    ReferralRequest getReferralRequestById(long id) throws ReferralRequestException;

    ReferralRequestDTO referTicketTo(TicketReferralDTO requestDTO) throws ReferralRequestException;

    void respondToReferralRequest(long referralRequestId, boolean response)
            throws ReferralRequestException;

    MembersDto refer() throws ReferralRequestException;

}
