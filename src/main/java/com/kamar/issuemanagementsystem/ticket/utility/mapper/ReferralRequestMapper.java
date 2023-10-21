package com.kamar.issuemanagementsystem.ticket.utility.mapper;

import com.kamar.issuemanagementsystem.ticket.data.dto.ReferralRequestDTO;
import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;

/**
 * the referral request mapper.
 * @author kamar baraka.*/

public interface ReferralRequestMapper {

    ReferralRequestDTO entityToDTO(ReferralRequest referralRequest);
}
