package com.kamar.issuemanagementsystem.user_management.controller;

import com.kamar.issuemanagementsystem.user_management.apis.UserEntityManagementApi;
import com.kamar.issuemanagementsystem.user_management.apis.UserEntityRegistrationAndActivationApi;
import com.kamar.issuemanagementsystem.user_management.dtos.UserEntityDto;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import com.kamar.issuemanagementsystem.user_management.models.UserEntityModel;
import com.kamar.issuemanagementsystem.user_management.service.UserEntityManagementService;
import com.kamar.issuemanagementsystem.user_management.service.UserEntityModelAssembler;
import com.kamar.issuemanagementsystem.user_management.service.UserEntityRoleManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.http.CacheControl;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


/**
 * The UserEntityManagementController class is responsible for handling HTTP requests related to managing user entities.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/users", "users"})
public class UserEntityManagementController implements UserEntityManagementApi {

    private final UserEntityManagementService service;
    private final UserEntityModelAssembler assembler;
    private final CacheControl cacheControl;

    /**
     * Retrieves a user entity by username.
     *
     * @param username the username of the user to retrieve
     * @return a ResponseEntity<UserEntityModel> representing the user if found,
     *         or an error response if the user is not found or an error occurred
     */
    @Operation(
                tags = {},
                summary = "Api to get a user by username",
                description = """
                        This endpoint is used to get detailed information about a user with a specific username.
                         To use this endpoint, you need to send a GET request with /api/v1/users?username={username} path,
                          where {username} is the username of the user you want to retrieve. The endpoint will either
                           return a UserEntityModel that represents the user with the provided username or an error
                            response if such a user doesn't exist or an error occurred.
                """,
                responses = {
                        @ApiResponse(
                                description = "Successful retrieval of the resource.",
                                responseCode = "200"
                        ),
                        @ApiResponse(
                                description = "Failed to locate the resource",
                                responseCode = "404"
                        )
                },
                security = {}
    )
    @GetMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Override
    public ResponseEntity<UserEntityModel> getUserByUsername(@RequestParam("username") String username) {

        /*get the user with the username*/
        UserEntity entity = (UserEntity) service.loadUserByUsername(username);
        /*get the model of the entity*/
        UserEntityModel model = assembler.toModel(entity);
        /*respond*/
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .lastModified(entity.getUpdatedOn())
                .body(model);
    }

    /**
     * Retrieves all users.
     *
     * @return a ResponseEntity containing a CollectionModel of UserEntityModel representing the list of all users
     */
    @Operation(
                tags = {},
                summary = "Api to get all users in the system.",
                description = """
                This endpoint is used to get a list of all users in the system. To use this endpoint,
                you need to send a GET request to /api/v1/users or /users. The endpoint will return a CollectionModel
                that represents all users in the system.
                
                Example:
                Request: GET /api/v1/users
                Response: [
                            {
                                "username": "user1",
                                "id": 1,
                                // additional fields
                            },
                            {
                                "username": "user2",
                                "id": 2,
                                // additional fields
                            },
                            // more users...
                           ]
                """,
                responses = {},
                security = {}
    )
    @GetMapping
    @Override
    public ResponseEntity<CollectionModel<UserEntityModel>> getAllUsers() {

        /*get all the users*/
        Iterable<UserEntity> entities = service.getAllUsers();
        CollectionModel<UserEntityModel> models = assembler.toCollectionModel(entities);
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .body(models);
    }

    /**
     * Updates the user with the specified username using the provided UserEntityDto.
     *
     * @param username the username of the user to update
     * @param dto the UserEntityDto containing the updated user information
     * @return a ResponseEntity<Void> indicating the result of the update operation
     */
    @Operation(
                tags = {},
                summary = "Api to update a user's details by their username.",
                description = """
                This endpoint is used to update user information for a specific user identified by their username. 
                To use this endpoint, you need to send a PATCH request to /api/v1/users?username={username}. 
                The request body should contain a JSON object that represents the UserEntityDto with the updated user information.
                
                Example:
                Request: PATCH /api/v1/users?username=johndoe
                Request Body: 
                {
                    "username": "johndoe",
                    "password": "newPassword123",
                    // additional fields
                }
                
                If the operation is successful, the endpoint will return a HTTP 204 (No Content) status code.
                If the username does not exist or an error occurs, the endpoint will return an error response.
                """,
                responses = {
                        @ApiResponse(
                                description = "Successful update of the user.",
                                responseCode = "204"
                        ),
                        @ApiResponse(
                                description = "Failed to find or update the user",
                                responseCode = "400"
                        )
                },
                security = {}
    )
    @PatchMapping
    @Override
    public ResponseEntity<Void> updateUser(@RequestParam("username") @Validated String username,
                                           @RequestBody @Validated UserEntityDto dto) {

        /*update the user*/
        UserDetails userDetails = service.loadUserByUsername(username);
        service.updateUser(userDetails);

        return ResponseEntity.noContent()
                .build();
    }

    /**
     * Deletes a user with the specified username.
     *
     * @param username the username of the user to be deleted
     * @return a ResponseEntity<Void> indicating the result of the operation
     */
    @Operation(
            tags = {},
            summary = "Delete a user by username",
            description = """
                Send a DELETE request with /api/v1/users?username={username} path,
                where {username} is the username of the user you want to delete.
                For example: DELETE /api/v1/users?username=johndoe.
                If successful, the response will be HTTP 204 (No Content).
                If the user doesn't exist, the response will be an error.
            """,
            responses = {
                    @ApiResponse(description = "Successful deletion of a user", responseCode = "204")
            }
    )
    @DeleteMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Override
    public ResponseEntity<Void> deleteUser(@RequestParam("username") String username) {

        /*delete the user*/
        service.deleteUser(username);
        return ResponseEntity.noContent()
                .build();
    }
}

/**
 * Represents a controller class for user registration and activation.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Controller
@RequestMapping(value = { "user"})
@RequiredArgsConstructor
class UserEntityRegistrationActivationController implements UserEntityRegistrationAndActivationApi{

    private final UserEntityManagementService userService;
    private final UserEntityRoleManagerService roleManagerService;

    /**
     * Registers a new user with the given user entity DTO.
     *
     * @param userEntityDto   The UserEntityDto object containing information of the user to be registered.
     * @param model The model to populate with registration status information.
     * @return A String indicating the view to display after the registration process.
     */
    @PostMapping(value = {"register"})
    @Override
    public String registerUser(@ModelAttribute("userEntityDto") UserEntityDto userEntityDto, Model model) {

        try {
            /*register the user*/
            userService.registerUser(userEntityDto);
            /*add the username as an attribute to pass to the next view*/
            model.addAttribute("roleList", roleManagerService.getAllUserEntityRoles());
            model.addAttribute("candidate", userEntityDto.username());

        } catch (Exception e) {

            model.addAttribute("registrationError",
                    "an error occurred while trying to register you! Try again.");

            return "user_registration_page";
        }
        /*return the activation view*/
        return "user_activation_page";
    }

    /**
     * Activates a user account using the provided activation code.
     *
     * @param activationCode The activation code to activate the user account.
     * @param model          The model to populate with activation status information.
     * @return A string indicating the view to display after the activation process.
     */
    @PostMapping(value = {"registration/activate"})
    @Override
    public String  activateUser(@RequestParam("activationCode") String activationCode, Model model) {

        try {
            /*get the username*/
            String  candidate = (String) model.getAttribute("candidate");
            /*activate the user*/
            userService.activateUser(candidate, activationCode);
            /*notify the results*/
            model.addAttribute("activationSuccess", "Your account has been activated successfully!");

        } catch (Exception e) {
            /*notify the error*/
            model.addAttribute("activationError",
                    "An error occurred while trying to activate your account. Please try again.");
            /*retry*/
            return "user_activation_page";
        }
        return "redirect:/login";
    }
}