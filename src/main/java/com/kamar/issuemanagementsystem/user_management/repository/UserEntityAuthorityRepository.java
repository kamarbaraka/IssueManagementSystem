package com.kamar.issuemanagementsystem.user_management.repository;

import com.kamar.issuemanagementsystem.user_management.entity.UserEntityAuthority;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The UserEntityAuthorityRepository interface provides methods for interacting with the user authority entity in the database.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */

@Repository
public interface UserEntityAuthorityRepository extends JpaRepository<UserEntityAuthority, Long> {

    /**
     * Finds a UserEntityAuthority by its authority.
     *
     * This method searches for a UserEntityAuthority in the database based on the provided authority name.
     * It returns an Optional<UserEntityAuthority> object that may contain the matching UserEntityAuthority if found,
     * or an empty Optional if no UserEntityAuthority is found.
     *
     * @param authority The authority name to search for.
     * @return An Optional<UserEntityAuthority> object that may contain the matching UserEntityAuthority if found,
     *         or an empty Optional if no UserEntityAuthority is found.
     */
    Optional<UserEntityAuthority> findUserEntityAuthorityByAuthority(final String authority);

    /**
     * Finds a slice of UserEntityAuthority objects.
     *
     * This method retrieves a slice of UserEntityAuthority objects from the database based on the provided pageable parameters.
     * It returns a Slice<UserEntityAuthority> object that represents a sublist of UserEntityAuthority objects.
     *
     * @param pageable The pageable parameters to control the pagination and sorting of the result.
     * @return A Slice<UserEntityAuthority> object that represents a sublist of UserEntityAuthority objects.
     */
    Slice<UserEntityAuthority> findAllSlice(@NotNull Pageable pageable);

    /**
     * Deletes a UserEntityAuthority by its authority.
     *
     * This method deletes a UserEntityAuthority from the database based on the provided authority name.
     * The UserEntityAuthority with the matching authority name will be removed from the database.
     *
     * @param authority The authority name of the UserEntityAuthority to delete.
     */
    void deleteUserEntityAuthorityByAuthority(@NotNull final String authority);

    /**
     * Checks if a UserEntityAuthority exists in the database based on the provided authority name.
     *
     * This method searches for a UserEntityAuthority in the database using the provided authority name.
     * It returns a boolean value indicating whether a UserEntityAuthority with the given authority name exists or not.
     *
     * @param authority The authority name to check for existence.
     * @return {@code true} if a UserEntityAuthority with the given authority name exists, {@code false} otherwise.
     * @throws NullPointerException if the authority parameter is null.
     */
    boolean existsUserEntityAuthorityByAuthority(@NotNull final String authority);

}