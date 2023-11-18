package com.kamar.issuemanagementsystem.ticket.data.dto;

import com.kamar.issuemanagementsystem.user.data.dto.DtoType;

import java.io.Serializable;
import java.util.List;

/**
 * ticket state exposure for admin.
 * @author kamar baraka.*/

public record TicketAdminPresentationDTO(

        long id,
        String title,
        String description,
        String priority,
        String status,
        String raisedBy,
        String assignedTo,
        String deadline,
        List<byte[]> attachments
) implements DtoType, Serializable {
}
