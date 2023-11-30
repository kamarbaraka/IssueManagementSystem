package com.kamar.issuemanagementsystem.ticket.data.dto;

import com.kamar.issuemanagementsystem.user.data.dto.DtoType;

import java.io.Serializable;

/**
 * ticket state exposure for admin.
 * @author kamar baraka.*/

public record TicketAdminPresentationDTO(

        String ticketNumber,
        String title,
        String description,
        String priority,
        String status,
        String raisedBy,
        String departmentAssigned,
        String assignedTo,
        String deadline,
        boolean hasAttachment
) implements DtoType, Serializable {
}
