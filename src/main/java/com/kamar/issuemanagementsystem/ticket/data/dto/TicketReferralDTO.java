package com.kamar.issuemanagementsystem.ticket.data.dto;

import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import jakarta.validation.constraints.Email;

import java.io.Serializable;

/**
 * the ticket referral DTO.
 * @author kamar baraka.*/

public record TicketReferralDTO(
        String ticketToRefer,
        @Email
        String to,
        String reason
) implements DtoType, Serializable {
}
