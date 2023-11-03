package com.kamar.issuemanagementsystem.user.data.dto;

import jakarta.validation.constraints.Email;

import java.io.Serializable;

/**
 * the user presentation DTO.
 * @author kamar baraka.*/

public record UserPresentationDTO(

        @Email
        String username,
        String authority,
        int rating
) implements DtoType, Serializable {
}
