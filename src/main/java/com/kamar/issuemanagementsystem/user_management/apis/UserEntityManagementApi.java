package com.kamar.issuemanagementsystem.user_management.apis;


import com.kamar.issuemanagementsystem.user_management.dtos.UserEntityDto;
import com.kamar.issuemanagementsystem.user_management.models.UserEntityModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.ResponseEntity;

/**
 * The UserEntityManagementApi interface provides methods for managing user entities.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public interface UserEntityManagementApi {

    /**
     * Retrieves a user entity by username.
     *
     * @param username the username of the user to retrieve
     * @return a ResponseEntity<UserEntityModel> representing the user if found,
     *         or an error response if the user is not found or an error occurred
     */
    ResponseEntity<UserEntityModel> getUserByUsername(@NotNull final String username);

    /**
     * Retrieves all users.
     *
     * @return a {@link ResponseEntity} that contains a {@link CollectionModel} of {@link UserEntityModel}
     *         representing the list of all users
     */
    ResponseEntity<CollectionModel<UserEntityModel>> getAllUsers();

    /**
     * Updates the user with the specified username using the provided UserEntityDto.
     *
     * @param username the username of the user to update
     * @param dto the UserEntityDto containing the updated user information
     * @return a ResponseEntity<Void> indicating the result of the update operation
     */
    ResponseEntity<Void> updateUser(@NotNull final String username, @NotNull UserEntityDto dto);

    /**
     * Deletes a user with the specified username.
     *
     * @param username the username of the user to be deleted
     * @return a ResponseEntity<Void> indicating the result of the operation
     */
    ResponseEntity<Void> deleteUser(@NotNull final String username);
}
