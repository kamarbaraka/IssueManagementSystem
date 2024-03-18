package com.kamar.issuemanagementsystem.user_management.service;

import com.kamar.issuemanagementsystem.user_management.dtos.UserEntityRoleDto;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntityRole;
import jakarta.validation.constraints.NotNull;

import java.util.List;

/**
 * The UserEntityRoleManager interface provides methods for managing user entity roles.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public interface UserEntityRoleManagerService {

    /**
     * Creates a new user entity role based on the provided UserEntityRoleDto.
     *
     * @param dto the UserEntityRoleDto object containing the role name and granted authorities for the new role.
     *            Cannot be null.
     * @throws NullPointerException if the dto parameter is null.
     */
    void createUserEntityRole(@NotNull UserEntityRoleDto dto);

    /**
     * Retrieves the {@link UserEntityRole} object associated with the provided role.
     *
     * @param role the role name of the user entity role. Cannot be null.
     * @return the {@link UserEntityRole} object representing the user entity role with the provided role.
     * @throws NullPointerException if the role parameter is null.
     */
    @NotNull
    UserEntityRole getUserEntityRoleByRole(@NotNull final String role);

    /**
     * Retrieves all user entity roles.
     *
     * @return a list of {@link UserEntityRole} objects representing all user entity roles.
     */
    @NotNull
    List<UserEntityRole> getAllUserEntityRoles();

    /**
     * Updates the user entity role with the provided role name using the information from the given UserEntityRoleDto.
     *
     * @param role the role name of the user entity role to be updated. Cannot be null.
     * @param dto the UserEntityRoleDto object containing the updated information for the role. Cannot be null.
     * @throws NullPointerException if either the role or dto parameter is null.
     */
    void updateUserEntityRole(@NotNull final String role, @NotNull UserEntityRoleDto dto);

    /**
     * Deletes the user entity role with the given role name.
     *
     * @param role the role name of the user entity role to be deleted. Cannot be null.
     */
    void deleteUserEntityRole(@NotNull final String role);
}
