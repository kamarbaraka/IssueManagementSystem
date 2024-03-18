package com.kamar.issuemanagementsystem.user_management.repository;

import com.kamar.issuemanagementsystem.user_management.entity.UserEntityRole;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * The UserEntityRoleRepository interface provides methods for retrieving and manipulating UserEntityRole objects in the database.
 *
 * This interface extends the JpaRepository interface, which provides basic CRUD operations for working with entities.
 * It specifies the generic type UserEntityRole as the first parameter, representing the entity class to be managed,
 * and the type of the primary key as the second parameter, in this case, String.
 *
 * The UserEntityRoleRepository interface also defines an additional method, findUserEntityRoleByRoleIgnoreCase,
 * for retrieving a UserEntityRole object based on its role, ignoring case sensitivity.
 *
 * This interface is annotated with @Repository to indicate that it is a Spring Data repository,
 * which allows it to be automatically detected and instantiated by Spring Boot.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Repository
public interface UserEntityRoleRepository extends JpaRepository<UserEntityRole, String> {

    /**
     * Retrieves an optional UserEntityRole by role, ignoring case sensitivity.
     *
     * @param role the role to search for (case insensitive)
     * @return an Optional containing the UserEntityRole if found, or an empty Optional if not found.
     */
    Optional<UserEntityRole> findUserEntityRoleByRoleIgnoreCase(@NotNull String role);
}