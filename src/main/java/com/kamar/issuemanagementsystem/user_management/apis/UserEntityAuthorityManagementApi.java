package com.kamar.issuemanagementsystem.user_management.apis;

import com.kamar.issuemanagementsystem.user_management.dtos.UserEntityAuthorityDto;
import com.kamar.issuemanagementsystem.user_management.models.UserEntityAuthorityModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.http.ResponseEntity;

/**
 * This interface provides methods for managing user entity authorities.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public interface UserEntityAuthorityManagementApi {

    /**
     * Creates a user entity authority using the provided DTO.
     *
     * This method creates a new user entity authority based on the provided DTO. The DTO must contain a non-null, non-empty, and non-blank authority value.
     *
     * @param dto the DTO containing the authority value for the user entity authority (must not be null)
     * @return a ResponseEntity object with a void response
     * @throws NullPointerException if the dto parameter is null
     */
    @NotNull
    ResponseEntity<Void> createUserEntityAuthority(@NotNull UserEntityAuthorityDto dto);

    /**
     * Retrieves a user entity authority by its authority.
     *
     * This method retrieves the user entity authority that matches the specified authority.
     *
     * @param authority the authority of the user entity authority (must not be null)
     * @return a ResponseEntity object containing the UserEntityAuthorityModel object that matches the specified authority
     * @throws NullPointerException if the authority parameter is null
     */
    @NotNull
    ResponseEntity<UserEntityAuthorityModel> getUserEntityAuthorityByAuthority(@NotNull String authority);

    /**
     * Retrieves a paginated list of all user entity authorities.
     *
     * This method returns a ResponseEntity object containing a Slice of UserEntityAuthorityModel objects, representing the paginated list of user entity authorities.
     *
     * The pageable parameter is used to specify the pagination information, such as the page number, page size, and sorting. It must not be null.
     *
     * Example usage:
     * <pre>{@code
     * Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "authority"));
     * ResponseEntity<Slice<UserEntityAuthorityModel>> response = getAllUserEntityAuthorities(pageable);
     * Slice<UserEntityAuthorityModel> userEntityAuthorities = response.getBody();
     * }</pre>
     *
     * @param pageable the pageable object containing the pagination information (must not be null)
     * @return a ResponseEntity object containing a Slice of UserEntityAuthorityModel objects
     * @throws NullPointerException if the pageable parameter is null
     */
    @NotNull
    ResponseEntity<Slice<UserEntityAuthorityModel>> getAllUserEntityAuthorities(@NotNull Pageable pageable);

    /**
     * Updates the authority of a user entity authority with the specified authority.
     *
     * @param authority the authority to be updated (must not be null)
     * @param dto the user entity authority DTO containing the updated authority (must not be null)
     * @return the ResponseEntity object with a void response
     * @throws IllegalArgumentException if the authority or dto parameter is null
     * @since 1.0.0
     */
    @NotNull
    ResponseEntity<Void> updateUserEntityAuthority(@NotNull String authority, @NotNull UserEntityAuthorityDto dto);

    /**
     * Deletes the user entity authority with the specified authority.
     *
     * @param authority the authority of the user entity authority to be deleted (must not be null)
     * @return the ResponseEntity object with a void response
     * @throws IllegalArgumentException if the authority parameter is null
     */
    @NotNull
    ResponseEntity<Void> deleteUserEntityAuthority(@NotNull String authority);

}
