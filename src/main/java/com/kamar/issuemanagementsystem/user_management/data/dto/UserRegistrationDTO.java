package com.kamar.issuemanagementsystem.user_management.data.dto;

import jakarta.validation.constraints.Email;

import java.io.Serializable;

/**
 * dto to hold user registration data.
 * @author kamar baraka.*/

public record UserRegistrationDTO(

        @Email(message = "not a valid email address")
        String username,
        /*@Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.\\d)",
        message = "password must contain at least one uppercase, lowercase letter and a special character " +
                "and minimum length of eight characters")*/
        String password,
        String role
) implements DtoType, Serializable {
}
