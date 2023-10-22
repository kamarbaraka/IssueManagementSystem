package com.kamar.issuemanagementsystem.user.data.dto;

import java.io.Serializable;

/**
 * the user presentation DTO.
 * @author kamar baraka.*/

public record UserPresentationDTO(

        String username,
        String authority,
        long totalStars
) implements DtoType, Serializable {
}
