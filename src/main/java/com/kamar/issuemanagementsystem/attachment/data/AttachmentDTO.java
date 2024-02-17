package com.kamar.issuemanagementsystem.attachment.data;

import com.kamar.issuemanagementsystem.user_management.data.dto.DtoType;
import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Size;

/**
 * the attachments DTO.
 * @author kamar baraka.*/


public record AttachmentDTO(

        @Nonnull
        String originalFilename,
        @Nonnull String contentType,
        @Nonnull @Size(max = (10 * 1024 * 1024))
        byte[] content
) implements DtoType {
}
