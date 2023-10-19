package com.kamar.issuemanagementsystem.user.data.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

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
        String password
) implements DtoType, Serializable {
}
