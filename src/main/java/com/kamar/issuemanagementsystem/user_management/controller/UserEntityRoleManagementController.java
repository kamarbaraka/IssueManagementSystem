package com.kamar.issuemanagementsystem.user_management.controller;

import com.kamar.issuemanagementsystem.user_management.apis.UserEntityRoleManagementApi;
import com.kamar.issuemanagementsystem.user_management.dtos.UserEntityRoleDto;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntityRole;
import com.kamar.issuemanagementsystem.user_management.models.UserEntityRoleModel;
import com.kamar.issuemanagementsystem.user_management.service.UserEntityRoleManagerService;
import com.kamar.issuemanagementsystem.user_management.service.UserEntityRoleModelAssembler;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;


/**
 * The UserEntityRoleManagementController class is responsible for managing user entity roles by exposing API endpoints.
 * It handles requests for creating, retrieving, updating, and deleting user entity roles.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/roles"})
public class UserEntityRoleManagementController implements UserEntityRoleManagementApi {

    private final UserEntityRoleManagerService roleManagerService;
    private final UserEntityRoleModelAssembler roleModelAssembler;
    private final CacheControl cacheControl;

    /**
     * Creates a new user entity role based on the provided UserEntityRoleDto.
     *
     * @param roleDto the UserEntityRoleDto object containing the role name and granted authorities for the new role. Cannot be null.
     * @return A ResponseEntity<Void> object representing the HTTP response with the location URI of the created user entity role.
     * @throws NullPointerException if the roleDto parameter is null.
     */
    @PostMapping
    @Override
    @NotNull
    public ResponseEntity<Void> createUserEntityRole(@NotNull @RequestBody UserEntityRoleDto roleDto) {

        /*create the user entity role*/
        roleManagerService.createUserEntityRole(roleDto);

        URI locationUri = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserEntityRoleManagementController.class)
                        .getUserEntityRoleByRole(roleDto.role())
        ).toUri();

        return ResponseEntity.created(locationUri).build();

    }

    /**
     * Retrieves all user entity roles.
     *
     * @return A ResponseEntity containing a CollectionModel of UserEntityRoleModel objects representing all user entity roles.
     */
    @GetMapping
    @Override
    @NotNull
    public ResponseEntity<CollectionModel<UserEntityRoleModel>> getAllUserEntityRoles() {

        /*get all the roles*/
        List<UserEntityRole> roles = roleManagerService.getAllUserEntityRoles();
        CollectionModel<UserEntityRoleModel> roleModels = roleModelAssembler.toCollectionModel(roles);
        return ResponseEntity.ok(roleModels);
    }

    /**
     * Retrieves the user entity role by role.
     *
     * @param role the role of the user entity role. Cannot be null.
     * @return the ResponseEntity<UserEntityRoleModel> object representing the user entity role with the provided role.
     * @throws NullPointerException if the role parameter is null.
     */
    @GetMapping
    @Override
    @NotNull
    public ResponseEntity<UserEntityRoleModel> getUserEntityRoleByRole(@NotNull @RequestParam("role") final String role) {

        /*get the entity with the role*/
        UserEntityRole entityRole = roleManagerService.getUserEntityRoleByRole(role);
        /*get the model*/
        UserEntityRoleModel model = roleModelAssembler.toModel(entityRole);
        /*respond*/
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .lastModified(entityRole.getUpdatedOn())
                .body(model);
    }


    /**
     * Update the role of a user entity role.
     *
     * This method updates the role of an existing user entity role in the system using the provided role name and UserEntityRoleDto.
     * It invokes the roleManagerService.updateUserEntityRole method to update the entity.
     *
     * @param role    The new role for the user entity role. Cannot be null.
     * @param roleDto The UserEntityRoleDto containing the new role and granted authorities information for the user entity role. Cannot be null.
     * @return ResponseEntity<Void> representing the outcome of the operation.
     *         If the user entity role is updated successfully, a ResponseEntity<Void> with HTTP status code 204 (No Content) is returned.
     * @throws NullPointerException if the role or roleDto parameter is null.
     */
    @PatchMapping
    @Override
    @NotNull
    public ResponseEntity<Void> updateUserEntityRole( @RequestParam("role") final String role,
                                                      @NotNull @RequestBody UserEntityRoleDto roleDto) {

        /*update the entity*/
        roleManagerService.updateUserEntityRole(role, roleDto);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes the user entity role with the provided role.
     *
     * This method deletes an existing user entity role from the system using the provided role.
     * The user entity role associated with the provided role will be removed from the database.
     *
     * @param role the role of the user entity role to be deleted. Cannot be null.
     * @return a ResponseEntity<Void> representing the outcome of the operation.
     * If the user entity role is deleted successfully, a ResponseEntity<Void> with HTTP status code 204 (No Content) is returned.
     * If the user entity role does not exist, a ResponseEntity<Void> with HTTP status code 404 (Not Found) is returned.
     * @throws NullPointerException if the role parameter is null.
     */
    @DeleteMapping
    @Override
    @NotNull
    public ResponseEntity<Void> deleteUserEntityRoleByRole(@NotNull @RequestParam("role") final String role) {

        /*delete the role*/
        roleManagerService.deleteUserEntityRole(role);

        return ResponseEntity.noContent().build();
    }
}
