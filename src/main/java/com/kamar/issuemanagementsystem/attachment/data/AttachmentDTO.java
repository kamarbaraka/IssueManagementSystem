package com.kamar.issuemanagementsystem.attachment.data;

import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import jakarta.validation.constraints.Size;
import org.springframework.web.multipart.MultipartFile;

/**
 * the attachment DTO.
 * @author kamar baraka.*/

public record AttachmentDTO(

        @Size(max = (10 * 1024 * 1024))
        MultipartFile attachment
) implements DtoType {
}
