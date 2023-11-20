package com.kamar.issuemanagementsystem.user.data.dto;

import jakarta.validation.constraints.Email;

import java.io.Serializable;
import java.util.List;

/**
 * the user presentation DTO.
 * @author kamar baraka.*/

public record UserPresentationDTO(

        @Email
        String username,
        List<String> authorities,
        int rating
) implements DtoType, Serializable {
}
