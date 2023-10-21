package com.kamar.issuemanagementsystem.ticket.data.dto;

import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import jakarta.validation.constraints.Size;

/**
 * the ticket creation DTO.
 * @author kamar baraka.*/


public record TicketCreationDTO(

        @Size(max = 50, message = "title too long")
        String title,
        @Size(max = 500, message = "description exceeds limit")
        String description
) implements DtoType {
}
