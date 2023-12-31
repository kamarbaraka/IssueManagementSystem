package com.kamar.issuemanagementsystem.ticket.data.dto;

import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.EqualsAndHashCode;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.List;

/**
 * the user feedback DTO.
 * @author kamar baraka.*/

public record TicketUserFeedbackDTO(

        String ticketNumber,
        String feedback,
        boolean satisfied,

        @Max(value = 5, message = "max rating is 5")
                @Min(value = 0)
        int serviceRating,
        List<MultipartFile> attachments
) implements DtoType, Serializable {
}
