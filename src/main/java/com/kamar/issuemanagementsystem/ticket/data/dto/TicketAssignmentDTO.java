package com.kamar.issuemanagementsystem.ticket.data.dto;

import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;

import java.io.Serializable;

/**
 * the ticket assignment DTO.
 * @author kamar baraka.*/

public record TicketAssignmentDTO(

        String ticketNumber,
        String assignTo,
        String priority,
        String deadline
) implements DtoType, Serializable {
}
