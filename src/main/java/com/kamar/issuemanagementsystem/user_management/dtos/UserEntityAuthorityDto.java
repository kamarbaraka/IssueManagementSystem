package com.kamar.issuemanagementsystem.user_management.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.kamar.issuemanagementsystem.user_management.entity.UserEntityAuthority}
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public record UserEntityAuthorityDto(
        @NotNull(message = "authority cannot be null.")
        @NotEmpty(message = "authority cannot be empty.")
        @NotBlank(message = "authority cannot be blank.")
        String authority
) implements Serializable {
}