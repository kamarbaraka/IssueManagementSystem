package com.kamar.issuemanagementsystem.user_management.apis;

import com.kamar.issuemanagementsystem.user_management.dtos.UserEntityDto;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;

/**
 * Represents an API for user registration and activation.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public interface UserEntityRegistrationAndActivationApi {

    /**
     * Registers a new user.
     *
     * @param dto   The UserEntityDto object containing information of the user to be registered.
     * @param model The model to populate with registration status information.
     * @return A message indicating the success or failure of the registration process.
     */
    String registerUser(@NotNull UserEntityDto dto, Model model);
    /**
     * Activates a user account using the provided activation code.
     *
     * @param activationCode The activation code to activate the user account.
     * @param model          The model to populate with activation status information.
     */
    String  activateUser( @NotNull final String activationCode, Model model);
}
