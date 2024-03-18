package com.kamar.issuemanagementsystem.user_management.service;

import com.kamar.issuemanagementsystem.user_management.dtos.UserEntityAuthorityDto;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntityAuthority;
import com.kamar.issuemanagementsystem.user_management.exceptions.UserEntityAuthorityReadException;
import com.kamar.issuemanagementsystem.user_management.exceptions.UserEntityAuthorityWriteException;
import com.kamar.issuemanagementsystem.user_management.repository.UserEntityAuthorityRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


/**
 * This class is responsible for managing user entity authorities.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class UserEntityAuthorityManager implements UserEntityAuthorityManagementService{

    private final UserEntityAuthorityRepository authorityRepository;

    /**
     * Creates and persists a UserEntityAuthority based on the provided UserEntityAuthorityDto object.
     *
     * @param dto The UserEntityAuthorityDto object containing the authority of the UserEntityAuthority to create. Must not be null.
     * @throws NullPointerException If the dto parameter is null.
     */
    @NotNull
    @Override
    public void createUserEntityAuthority(@NotNull UserEntityAuthorityDto dto) {

        /*build the entity*/
        UserEntityAuthority userEntityAuthority = UserEntityAuthority.builder()
                .authority(dto.authority())
                .build();

        /*persist*/
        authorityRepository.save(userEntityAuthority);

    }

    /**
     * Retrieves a UserEntityAuthority object by its authority.
     *
     * This method retrieves a UserEntityAuthority object from the database based on the provided authority name.
     * It searches for a UserEntityAuthority with the matching authority name and returns it.
     * If no UserEntityAuthority is found with the provided authority name,
     * a UserEntityAuthorityReadException is thrown.
     *
     * @param authority The authority name of the UserEntityAuthority to retrieve. Must not be null.
     * @return The UserEntityAuthority object with the matching authority name.
     * @throws UserEntityAuthorityReadException If no UserEntityAuthority is found with the provided authority name.
     * @throws NullPointerException If the authority parameter is null.
     */
    @NotNull
    @Override
    public UserEntityAuthority getUserEntityAuthorityByAuthority(@NotNull String authority) {

        /*get the entity with the authority*/
        return authorityRepository.findUserEntityAuthorityByAuthority(authority)
                .orElseThrow(() -> new UserEntityAuthorityReadException("failed to get the authority!"));
    }

    /**
     * Retrieves a slice of UserEntityAuthority objects based on the provided Pageable object.
     *
     * <p>This method retrieves a slice of UserEntityAuthority objects from the database based on the provided pageable parameters.
     * It returns a Slice<UserEntityAuthority> object that represents a sublist of UserEntityAuthority objects.</p>
     *
     * @param pageable The pageable parameters to control the pagination and sorting of the result. Must not be null.
     * @return A Slice<UserEntityAuthority> object that represents a sublist of UserEntityAuthority objects.
     * @throws IllegalArgumentException If the pageable parameter is null.
     * @see Pageable
     * @see Slice
     */
    @Override
    public Slice<UserEntityAuthority> getUserEntityAuthoritiesWithSlice(@NotNull Pageable pageable) {

        /*get all entities in the database*/
        return authorityRepository.findAllSlice(pageable);
    }

    /**
     * Updates the authority of a UserEntityAuthority in the database.
     *
     * This method updates the authority of a UserEntityAuthority in the database based on the provided authority name.
     * It searches for the UserEntityAuthority with the given authority name and if found, updates its authority value with
     * the authority value from the UserEntityAuthorityDto parameter. If no UserEntityAuthority is found with the given authority name,
     * a UserEntityAuthorityWriteException is thrown.
     *
     * @param authority The authority name of the UserEntityAuthority to update. Must not be null.
     * @param dto The UserEntityAuthorityDto object containing the new authority value. Must not be null.
     *
     * @throws UserEntityAuthorityWriteException If no UserEntityAuthority is found with the given authority name,
     *                                          or an error occurs while trying to update the authority.
     *                                          The exception message will provide more details about the error.
     * @throws NullPointerException If the authority or dto parameter is null.
     */
    @Override
    public void updateUserEntityAuthority(@NotNull String authority, @NotNull UserEntityAuthorityDto dto) {

        /*update the user entity*/
        UserEntityAuthority userEntityAuthority = authorityRepository.findUserEntityAuthorityByAuthority(authority)
                .orElseThrow(() ->
                        new UserEntityAuthorityWriteException("Authority not An error occurred while trying to update!"));

        userEntityAuthority.setAuthority(dto.authority());

        authorityRepository.save(userEntityAuthority);

    }

    /**
     * Deletes a UserEntityAuthority from the database based on the provided authority.
     * If the authority does not exist, a UserEntityAuthorityWriteException is thrown.
     *
     * @param authority The authority of the UserEntityAuthority to delete. Must not be null.
     * @throws UserEntityAuthorityWriteException if the authority does not exist in the database.
     * @throws NullPointerException if the authority is null.
     */
    @Override
    public void deleteUserEntityAuthorityByAuthority(@NotNull String authority) {

        /*delete the entity*/
        if (!authorityRepository.existsUserEntityAuthorityByAuthority(authority)) {
            throw new UserEntityAuthorityWriteException("Failed to delete authority! Try again");
        }

        authorityRepository.deleteUserEntityAuthorityByAuthority(authority);
    }
}
