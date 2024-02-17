package com.kamar.issuemanagementsystem.user_management.data.dto;

import jakarta.validation.constraints.Email;

/**
 * the user activation DTO.
 * @author kamar baraka.*/

public record UserActivationDTO(

        @Email
        String username,
        String token
) implements DtoType {
}
