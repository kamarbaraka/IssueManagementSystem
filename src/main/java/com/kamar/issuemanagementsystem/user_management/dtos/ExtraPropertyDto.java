package com.kamar.issuemanagementsystem.user_management.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.io.Serializable;

/**
 * DTO for {@link com.kamar.issuemanagementsystem.user_management.entity.ExtraProperty}
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public record ExtraPropertyDto(
        @NotNull(message = "key cannot be null.")
        @NotEmpty(message = "key cannot be empty.")
        @NotBlank(message = "key cannot be blank.")
        String key,
        String value
) implements Serializable {
}