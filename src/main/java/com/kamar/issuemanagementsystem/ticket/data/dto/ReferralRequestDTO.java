package com.kamar.issuemanagementsystem.ticket.data.dto;

import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import jakarta.validation.constraints.Pattern;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * the DTO for creating a referral request.
 * @author kamar baraka.*/

public record ReferralRequestDTO(
        String referredTicket,
        @Pattern(regexp = "^(?=.*'@')")
        String to,
        String from,
        boolean accepted,
        LocalDate requestedOn
) implements DtoType, Serializable {
}
