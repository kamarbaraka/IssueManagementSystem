package com.kamar.issuemanagementsystem.client_management.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link com.kamar.issuemanagementsystem.client_management.entities.RegisteredClientEntity}
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public record RegisteredClientEntityDto(
        @NotNull(message = "client name cannot be null.")
        @NotEmpty(message = "client name cannot be empty.")
        @NotBlank(message = "client name cannot be blank.")
        String clientName,
        @NotNull(message = "redirect uri cannot be null.")
        @NotEmpty(message = "redirect uri cannot be empty.")
        @NotBlank(message = "redirect uri cannot be blank.")
        String redirectUri,
        @NotNull(message = "post logout redirect uri cannot be null.")
        @NotEmpty(message = "post logout redirect uri cannot be empty.")
        @NotBlank(message = "post logout redirect uri cannot be blank.")
        String postLogoutRedirectUri,
        Set<AuthorizationGrantType> authorizationGrantTypes
) implements Serializable {
}