package com.kamar.issuemanagementsystem.user_management.dtos;

import com.kamar.issuemanagementsystem.user_management.entity.UserEntityRole;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;
import java.util.Set;

/**
 * DTO for {@link UserEntityRole}
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public record UserEntityRoleDto(
        @NotNull(message = "role cannot be null.")
        @NotEmpty(message = "role cannot be empty.")
        @NotBlank(message = "role cannot be blank.")
        String role,
        @NotNull(message = "granted authorities cannot be null.")
        Set<GrantedAuthority> grantedAuthorities
) implements Serializable {
}