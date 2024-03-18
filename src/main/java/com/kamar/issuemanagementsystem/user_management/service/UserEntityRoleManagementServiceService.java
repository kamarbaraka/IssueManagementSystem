package com.kamar.issuemanagementsystem.user_management.service;

import com.kamar.issuemanagementsystem.user_management.dtos.UserEntityRoleDto;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntityRole;
import com.kamar.issuemanagementsystem.user_management.exceptions.UserEntityRoleUpdateException;
import com.kamar.issuemanagementsystem.user_management.repository.UserEntityRoleRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


/**
 * The UserEntityRoleManagementService class is a service class that implements the UserEntityRoleManager interface.
 * It provides methods for managing user entity roles, such as creating, retrieving, updating, and deleting roles.
 *
 * This class is annotated with @Service to indicate that it is a Spring service component,
 * which allows it to be automatically detected and instantiated by Spring Boot.
 *
 * The class implements the UserEntityRoleManager interface, which defines the methods for managing user entity roles.
 * It depends on the UserEntityRoleRepository interface for retrieving and manipulating UserEntityRole objects in the database.
 *
 * The class is instantiated with a UserEntityRoleRepository instance, which is injected via constructor injection using the @RequiredArgsConstructor annotation.
 *
 * The class provides the following public methods for managing user entity roles:
 * - createUserEntityRole(UserEntityRoleDto dto): Creates a new user entity role based on the provided UserEntityRoleDto.
 * - getUserEntityRoleByRole(String role): Retrieves the UserEntityRole object associated with the provided role.
 * - getAllUserEntityRoles(): Retrieves all user entity roles.
 * - updateUserEntityRole(UserEntityRoleDto dto): Updates the role and granted authorities of a user entity role.
 * - deleteUserEntityRole(String role): Deletes the user entity role with the given role name.
 *
 * These methods are implemented according to the business logic of managing user entity roles.
 * They interact with the UserEntityRoleRepository to perform database operations.
 *
 * @see UserEntityRoleManagerService
 * @see UserEntityRoleRepository
 * @see UserEntityRoleDto
 * @see UserEntityRole
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserEntityRoleManagementServiceService implements UserEntityRoleManagerService {

    private final UserEntityRoleRepository roleRepository;

    /**
     * Creates a new user entity role based on the provided UserEntityRoleDto.
     *
     * @param dto the UserEntityRoleDto object containing the role name and granted authorities for the new role. Cannot be null.
     * @throws NullPointerException if the dto parameter is null.
     */
    @Override
    public void createUserEntityRole(UserEntityRoleDto dto) {

        /*build a UserEntityRole instance and persist*/
        UserEntityRole userEntityRole = UserEntityRole.builder()
                .role(dto.role())
                .grantedAuthorities(dto.grantedAuthorities())
                .build();

        roleRepository.save(userEntityRole);
    }

    /**
     * Retrieves the {@link UserEntityRole} object associated with the provided role.
     *
     * @param role the role name of the user entity role. Cannot be null.
     * @return the {@link UserEntityRole} object representing the user entity role with the provided role.
     * @throws NullPointerException if the role parameter is null.
     */
    @NotNull
    @Override
    public UserEntityRole getUserEntityRoleByRole(@NotNull final String role) {

        /*get the user entity role with the provided role.*/
        return roleRepository.findUserEntityRoleByRoleIgnoreCase(role)
                .orElseThrow();
    }

    /**
     * Retrieves all user entity roles.
     *
     * @return a list of {@link UserEntityRole} objects representing all user entity roles.
     */
    @Override
    public List<UserEntityRole> getAllUserEntityRoles() {

        /*get all the user entity roles*/
        return roleRepository.findAll();
    }

    /**
     * Updates the role and granted authorities of a user entity role.
     *
     * @param dto the UserEntityRoleDto object containing the updated role and granted authorities. Cannot be null.
     * @throws UserEntityRoleUpdateException if an error occurs while trying to update the user entity role.
     * @throws NullPointerException if the dto parameter is null.
     */
    @Override
    public void updateUserEntityRole(final String role, @NotNull UserEntityRoleDto dto) {

        /*update the role*/
        UserEntityRole userEntityRole = roleRepository.findUserEntityRoleByRoleIgnoreCase(role)
                .orElseThrow(() -> new UserEntityRoleUpdateException("An error occurred while trying to update! Try again."));
        userEntityRole.setGrantedAuthorities(dto.grantedAuthorities());
        roleRepository.save(userEntityRole);

    }

    /**
     * Deletes the user entity role with the given role name.
     *
     * @param role the role name of the user entity role to be deleted. Cannot be null.
     */
    @Override
    public void deleteUserEntityRole(String role) {

        /*delete the role*/
        roleRepository.findUserEntityRoleByRoleIgnoreCase(role)
                .ifPresent(roleRepository::delete);

    }
}
