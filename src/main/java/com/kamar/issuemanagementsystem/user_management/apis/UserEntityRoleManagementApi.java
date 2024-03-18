package com.kamar.issuemanagementsystem.user_management.apis;

import com.kamar.issuemanagementsystem.user_management.dtos.UserEntityRoleDto;
import com.kamar.issuemanagementsystem.user_management.models.UserEntityRoleModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

/**
 * The UserEntityRoleManagementApi interface provides methods to manage user entity roles.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public interface UserEntityRoleManagementApi {

    /**
     * Creates a user entity role with the provided UserEntityRoleDto.
     *
     * This method creates a new user entity role in the system using the provided UserEntityRoleDto.
     * The UserEntityRoleDto contains the role and granted authorities information for the user entity role.
     * The created user entity role is then stored in the database.
     *
     * @param roleDto The UserEntityRoleDto containing the role and granted authorities information for the user entity role. Cannot be null.
     * @return ResponseEntity<Void> representing the outcome of the operation.
     *         If the user entity role is created successfully, a ResponseEntity<Void> with HTTP status code 201 (Created) is returned.
     *         If the user entity role already exists, a ResponseEntity<Void> with HTTP status code 409 (Conflict) is returned.
     * @throws IllegalArgumentException if the roleDto parameter is null.
     */
    @NotNull
    ResponseEntity<Void> createUserEntityRole(@NotNull UserEntityRoleDto roleDto);

    /**
     * Retrieves all UserEntityRoleModels.
     *
     * This method retrieves all UserEntityRoleModels from the system. It returns a ResponseEntity containing a CollectionModel
     * of UserEntityRoleModel objects representing all the user entity roles in the system. If there are no user entity roles
     * found, an empty CollectionModel is returned.
     *
     * @return ResponseEntity<CollectionModel < UserEntityRoleModel>> - a ResponseEntity containing a CollectionModel of
     *         UserEntityRoleModel objects representing all the user entity roles in the system.
     */
    @NotNull
    ResponseEntity<CollectionModel<UserEntityRoleModel>> getAllUserEntityRoles();
    /**
     * Retrieves the user entity role by role.
     *
     * @param role the role of the user entity role. Cannot be null.
     * @return the ResponseEntity<UserEntityRoleModel> object representing the user entity role with the provided role.
     * @throws NullPointerException if the role parameter is null.
     */
    @NotNull
    ResponseEntity<UserEntityRoleModel> getUserEntityRoleByRole(@NotNull final String role);

    /**
     * Update the role of the user entity role with the provided UserEntityRoleDto.
     *
     * This method updates the role of an existing user entity role in the system using the provided UserEntityRoleDto.
     * The UserEntityRoleDto contains the new role and granted authorities information for the user entity role.
     * The updated user entity role is then stored in the database.
     *
     * @param role    The new role for the user entity role. Cannot be null.
     * @param roleDto The UserEntityRoleDto containing the new role and granted authorities information for the user entity role. Cannot be null.
     * @return ResponseEntity<Void> representing the outcome of the operation.
     *         If the user entity role is updated successfully, a ResponseEntity<Void> with HTTP status code 200 (OK) is returned.
     *         If the user entity role does not exist, a ResponseEntity<Void> with HTTP status code 404 (Not Found) is returned.
     * @throws NullPointerException     if the role or roleDto parameter is null.
     * @throws IllegalArgumentException if the roleDto parameter is missing required fields or contains invalid values.
     */
    @NotNull
    ResponseEntity<Void> updateUserEntityRole(@NotNull final String role, @NotNull UserEntityRoleDto roleDto);

    /**
     * Deletes the user entity role with the provided role.
     *
     * <p>
     * This method deletes an existing user entity role from the system using the provided role.
     * The user entity role associated with the provided role will be removed from the database.
     * </p>
     *
     * @param role the role of the user entity role to be deleted. Cannot be null.
     * @return a ResponseEntity<Void> representing the outcome of the operation.
     *         If the user entity role is deleted successfully, a ResponseEntity<Void> with HTTP status code 204 (No Content) is returned.
     *         If the user entity role does not exist, a ResponseEntity<Void> with HTTP status code 404 (Not Found) is returned.
     * @throws NullPointerException if the role parameter is null.
     */
    @NotNull
    ResponseEntity<Void> deleteUserEntityRoleByRole(@NotNull final String role);
}
