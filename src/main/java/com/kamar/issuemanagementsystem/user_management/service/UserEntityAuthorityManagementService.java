package com.kamar.issuemanagementsystem.user_management.service;

import com.kamar.issuemanagementsystem.user_management.dtos.UserEntityAuthorityDto;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntityAuthority;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

/**
 * This interface defines the methods for managing user entity authorities.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public interface UserEntityAuthorityManagementService {

    /**
     * Creates a user entity authority based on the provided UserEntityAuthorityDto.
     *
     * @param dto the UserEntityAuthorityDto object containing the authority value
     * @throws IllegalArgumentException if the dto parameter is null
     */
    @NotNull
    void createUserEntityAuthority(@NotNull UserEntityAuthorityDto dto);

    /**
     * Retrieves a user authority entity based on the authority.
     *
     * @param authority the authority of the user authority entity to retrieve
     * @return the user authority entity with the specified authority
     * @throws IllegalArgumentException if the authority parameter is null
     */
    @NotNull
    UserEntityAuthority getUserEntityAuthorityByAuthority(@NotNull String authority);

    /**
     * Retrieves a slice of UserEntityAuthority objects based on the provided Pageable object.
     *
     * <p>This method retrieves a slice of UserEntityAuthority objects based on the provided Pageable object, which
     * specifies the page number, page size, and sorting criteria for the result. The Pageable object must not be null.
     * The method returns a slice of UserEntityAuthority objects that satisfy the specified Pageable criteria.</p>
     *
     * @param pageable the Pageable object specifying the page number, page size, and sorting criteria
     * @return a slice of UserEntityAuthority objects based on the specified Pageable criteria
     * @throws IllegalArgumentException if the pageable parameter is null
     * @see Pageable
     * @see Slice
     */
    Slice<UserEntityAuthority> getUserEntityAuthoritiesWithSlice(@NotNull Pageable pageable);

    /**
     * Updates the authority of a user entity.
     *
     * <p>This method updates the authority of a user entity based on the provided authority. The authority is validated
     * against the given UserEntityAuthorityDto object. The authority value must not be null, empty, or blank. If the provided
     * authority does not match any existing user entity authority, an exception will be thrown.</p>
     *
     * @param authority the new authority value for the user entity
     * @param dto the data transfer object representing the user entity authority
     * @throws IllegalArgumentException if the provided authority is null, empty, or blank
     * @throws EntityNotFoundException if the provided authority does not match any existing user entity authority
     */
    void updateUserEntityAuthority(@NotNull String authority, @NotNull UserEntityAuthorityDto dto);

    /**
     * Deletes a user entity authority based on the authority.
     *
     * @param authority the authority to delete
     */
    void deleteUserEntityAuthorityByAuthority(@NotNull String authority);
}
