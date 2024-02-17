package com.kamar.issuemanagementsystem.user_management.service;

import com.kamar.issuemanagementsystem.user_management.dtos.UserEntityDto;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.provisioning.UserDetailsManager;

import java.util.List;

/**
 * The UserEntityManagementService interface provides methods for managing user entities.
 * It extends the UserDetailsManager interface, which is part of the Spring Security framework.
 * Implementations of this interface should handle the registration of new users.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public interface UserEntityManagementService extends UserDetailsManager {

    /**
     * Register a new user with the given user entity DTO.
     *
     * @param dto the user entity DTO containing the user details
     * @throws NullPointerException if the DTO is null
     * @see UserEntityDto
     */
    default void registerUser(@NotNull UserEntityDto dto){}

    /**
     * Activates a user by setting the activation code of the user to null.
     *
     * @param username The username of the user to activate.
     * @param activationCode The activation code of the user.
     */
    default void activateUser(@NotNull final String username, @NotNull final String activationCode){}

    /**
     * Retrieves a list of all user entities.
     *
     * @return a list of UserEntity objects representing all users
     */
    @NotNull
    List<UserEntity> getAllUsers();


}
