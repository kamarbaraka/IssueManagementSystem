package com.kamar.issuemanagementsystem.ticket.data.dto;

import com.kamar.issuemanagementsystem.attachment.data.AttachmentDTO;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * the ticket creation DTO.
 * @author kamar baraka.*/


public record TicketCreationDTO(

        @Size(max = 50, message = "title too long")
        String title,

        @Size(max = 500, message = "description exceeds limit")
        String description,

        String departmentToAssign,

        List<@Size(max = (10 * 1024 * 1024)) MultipartFile> attachments

) implements DtoType {
}
