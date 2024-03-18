package com.kamar.issuemanagementsystem.user_management.service;


import com.kamar.issuemanagementsystem.user_management.dtos.ExtraPropertyDto;
import com.kamar.issuemanagementsystem.user_management.dtos.UserEntityDto;
import com.kamar.issuemanagementsystem.user_management.entity.ExtraProperty;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntityRole;
import com.kamar.issuemanagementsystem.user_management.exceptions.UserActivationException;
import com.kamar.issuemanagementsystem.user_management.exceptions.UserRegistrationException;
import com.kamar.issuemanagementsystem.user_management.repository.UserEntityRepository;
import com.kamar.issuemanagementsystem.user_management.repository.UserEntityRoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

/**
 * JpaUserEntityManagementService is an implementation of the UserEntityManagementService interface
 * that uses JPA and Spring Data JPA to interact with the user table in the database.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class JpaUserEntityManagementService implements UserEntityManagementService{

    /**
     * The repository for the UserEntity entity.
     */
    private final UserEntityRepository repository;

    private final UserEntityRoleRepository roleRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new user with the given user details.
     *
     * @param user the UserDetails object representing the user to be created
     */
    @Override
    public void createUser(UserDetails user) {

    }

    /**
     * Updates a user in the system.
     *
     * @param user the UserDetails object representing the user to be updated
     */
    @Override
    public void updateUser(UserDetails user) {

        /*check if the user exists*/
        if (!this.userExists(user.getUsername())) {
            /*throw an exception if user does not exist*/
            throw new UsernameNotFoundException("User does not exist");
        }
        /*update the user*/
        repository.save((UserEntity) user);
    }

    /**
     * Deletes a user with the given username.
     *
     * @param username the username of the user to delete
     * @throws UsernameNotFoundException if the user doesn't exist
     */
    @Override
    public void deleteUser(String username) {

        /*check if the user exists*/
        if (!this.userExists(username)) {
            /*throw an exception if the user doesn't exist*/
            throw new UsernameNotFoundException("cannot delete a ghost user!");
        }
        /*delete the user*/
        repository.deleteUserByUsername(username);
    }

    @Override
    public void changePassword(String oldPassword, String newPassword) {

    }

    /**
     * Checks if a user with the given username exists.
     *
     * @param username the username to check for existence
     * @return true if a user with the given username exists, false otherwise
     */
    @Override
    public boolean userExists(String username) {

        /*check if user exists*/
        return repository.existsByUsername(username);
    }

    /**
     * Loads a user by their username.
     *
     * @param username the username of the user to load.
     * @return the user details for the given username.
     * @throws UsernameNotFoundException if the user is not found.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        /*load the user if he exists*/
        return repository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("no such user in the system."));
    }

    /**
     * Registers a new user with the given user entity DTO.
     *
     * @param dto The UserEntityDto object representing the user to be registered.
     * @throws UserRegistrationException If an error occurs while trying to register the user.
     */
    @Override
    public void registerUser(UserEntityDto dto) {
        try
        {
            /*get the granted authorities*/
            UserEntityRole userEntityRole = roleRepository.findUserEntityRoleByRoleIgnoreCase(dto.role())
                    .orElseThrow();

            /*register the new user*/
            UserEntity userEntity = UserEntity.builder()
                    .username(dto.username())
                    .email(dto.email())
                    .password(passwordEncoder.encode(dto.password()))
                    .extraProperties(dto.extraProperties().stream()
                            .map(ExtraProperty::dtoToEntity).collect(Collectors.toSet()))
                    .authorities(userEntityRole.getGrantedAuthorities())
                    .build();
            /*encode the activation token*/
            userEntity.setActivationToken(passwordEncoder.encode(userEntity.getActivationToken()));

            /*persist the entity*/
            repository.save(userEntity);
        }
        catch (Exception e){
            /*throw a user registration exception*/
            throw new UserRegistrationException("an error occurred while trying to register! Try again.", e);
        }
    }

    /**
     * Activates a user by validating the activation code.
     *
     * @param username the username of the user to activate
     * @param activationCode the activation code of the user
     * @throws UserActivationException if the activation code is invalid
     */
    @Override
    public void activateUser(String username, String activationCode) {

        /*get the username and the activation code*/
        UserEntity entity = (UserEntity) this.loadUserByUsername(username);

        /*check whether the codes match*/
        if (!passwordEncoder.matches(activationCode, entity.getActivationToken())) {

            /*throw an exception*/
            throw new UserActivationException("failed to validate the activation code.");
        }
    }

    /**
     * Retrieves a list of all user entities.
     *
     * @return a list of UserEntity objects representing all users
     */
    @Override
    public List<UserEntity> getAllUsers() {

        /*get all the users*/
        return repository.findAll();
    }

}
