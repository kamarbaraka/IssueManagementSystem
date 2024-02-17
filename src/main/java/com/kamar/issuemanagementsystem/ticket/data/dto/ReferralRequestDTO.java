package com.kamar.issuemanagementsystem.ticket.data.dto;

import com.kamar.issuemanagementsystem.user_management.data.dto.DtoType;
import jakarta.validation.constraints.Email;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * the DTO for creating a referral request.
 * @author kamar baraka.*/

public record ReferralRequestDTO(

        long referralId,
        String referredTicket,
        @Email
        String to,
        String reason,
        @Email
        String from,
        boolean accepted,
        LocalDate requestedOn
) implements DtoType, Serializable {
}
