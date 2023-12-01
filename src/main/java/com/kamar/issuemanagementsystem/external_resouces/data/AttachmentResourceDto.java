package com.kamar.issuemanagementsystem.external_resouces.data;

import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import org.springframework.core.io.ByteArrayResource;


/**
 * the dto to hold email attachments details.
 * @author kamar baraka.*/

public record AttachmentResourceDto(

        String filename,
        ByteArrayResource attachment

) implements DtoType {
}
