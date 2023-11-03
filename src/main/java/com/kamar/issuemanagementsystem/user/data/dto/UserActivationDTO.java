package com.kamar.issuemanagementsystem.user.data.dto;

import jakarta.validation.constraints.Email;

import java.io.Serializable;

/**
 * the user activation DTO.
 * @author kamar baraka.*/

public record UserActivationDTO(

        @Email
        String username,
        String token
) implements DtoType {
}
