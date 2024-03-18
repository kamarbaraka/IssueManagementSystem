package com.kamar.issuemanagementsystem.client_management.repositories;

import com.kamar.issuemanagementsystem.client_management.entities.RegisteredClientEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

/**
 * The RegisteredClientEntityRepository interface provides methods for managing RegisteredClientEntity objects in the system.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public interface RegisteredClientEntityRepository extends JpaRepository<RegisteredClientEntity, String > {

    /**
     * Finds a RegisteredClientEntity by its clientId.
     *
     * @param clientId The clientId of the RegisteredClientEntity to find.
     * @return An Optional containing the RegisteredClientEntity if found, or an empty Optional if not found.
     */
    Optional<RegisteredClientEntity> findRegisteredClientEntityByClientId(@NotNull final String clientId);
    /**
     * Finds RegisteredClientEntity objects by the client name.
     *
     * @param clientName The name of the client to search for. Cannot be null.
     * @return A Slice of RegisteredClientEntity objects matching the client name.
     */
    Slice<RegisteredClientEntity> findRegisteredClientEntitiesByClientName(@NotNull final String clientName);

    /**
     * Retrieves a slice of all registered client entities based on the provided pageable object.
     *
     * @param pageable The pageable object specifying the page number, page size, and sorting criteria. Must not be null.
     * @return A slice containing the registered client entities.
     */
    Slice<RegisteredClientEntity> findAllWithSlice(@NotNull Pageable pageable);
}