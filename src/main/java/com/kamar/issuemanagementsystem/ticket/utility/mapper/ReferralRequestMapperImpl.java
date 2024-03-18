/*
package com.kamar.issuemanagementsystem.ticket.utility.mapper;

import com.kamar.issuemanagementsystem.ticket.data.dto.ReferralRequestDTO;
import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;
import org.springframework.stereotype.Service;

*/
/**
 * implementation of referral request mapper.
 * @author kamar baraka.*//*


@Service
public class ReferralRequestMapperImpl implements ReferralRequestMapper{
    @Override
    public ReferralRequestDTO entityToDTO(ReferralRequest referralRequest) {

        */
/*create an instance*//*

        return new ReferralRequestDTO(
                referralRequest.getRequestId(),
                referralRequest.getRefferedTicket().getTicketNumber(),
                referralRequest.getTo().getUsername(),
                referralRequest.getReason(),
                referralRequest.getFrom().getUsername(),
                referralRequest.isAccepted(),
                referralRequest.getRequestedOn()
        );
    }
}
*/
