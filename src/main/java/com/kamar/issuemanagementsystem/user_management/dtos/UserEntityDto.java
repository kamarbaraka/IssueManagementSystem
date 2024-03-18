package com.kamar.issuemanagementsystem.user_management.dtos;

import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link UserEntity}
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public record UserEntityDto(
        @NotNull(message = "username cannot be null.")
        @NotEmpty(message = "username cannot be empty.")
        @NotBlank(message = "username cannot be blank.")
        String username,
        @NotNull(message = "email cannot be null.")
        @Email(message = "value must be a valid email.")
        @NotEmpty(message = "email cannot be empty.")
        @NotBlank(message = "email cannot be blank.")
        String email,
        String role,
        @NotNull(message = "password cannot be null.")
        @NotEmpty(message = "password cannot be empty.")
        @NotBlank(message = "password cannot be blank.")
        String password,
        Set<ExtraPropertyDto> extraProperties
) implements Serializable {
}