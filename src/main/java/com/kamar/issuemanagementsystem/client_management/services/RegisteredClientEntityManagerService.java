package com.kamar.issuemanagementsystem.client_management.services;

import com.kamar.issuemanagementsystem.client_management.dtos.RegisteredClientEntityDto;
import com.kamar.issuemanagementsystem.client_management.entities.RegisteredClientEntity;
import com.kamar.issuemanagementsystem.client_management.models.RegisteredClientEntityModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;

/**
 * The RegisteredClientEntityManagerService interface represents a service for managing registered client entities in the system.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public interface RegisteredClientEntityManagerService extends RegisteredClientRepository {

    /**
     * Registers a client entity in the system.
     *
     * This method takes a {@link RegisteredClientEntityDto} object containing the client information and registers
     * the client entity in the system. It returns a {@link RegisteredClientEntityModel} object representing the registered
     * client entity.
     *
     * @param dto The {@link RegisteredClientEntityDto} object containing the client information (must not be null).
     * @return A {@link RegisteredClientEntityModel} object representing the registered client entity.
     */
    RegisteredClientEntityModel registerClientEntity(@NotNull RegisteredClientEntityDto dto);

    /**
     * Regenerates the client secret for the specified client ID.
     *
     * This method takes a client ID as input, generates a new client secret, and updates the client entity
     * in the system with the new client secret. It returns a {@link RegisteredClientEntityModel} object
     * representing the updated client entity.
     *
     * @param clientId The client ID for which to regenerate the client secret (must not be null).
     * @return A {@link RegisteredClientEntityModel} object representing the updated client entity.
     * @throws NullPointerException if {@code clientId} is null.
     */
    @NotNull
    RegisteredClientEntityModel regenerateClientSecret(@NotNull String clientId);

    /**
     * Retrieves a RegisteredClientEntity object based on the specified client ID.
     *
     * @param clientId the client ID corresponding to the RegisteredClientEntity to retrieve (must not be null)
     * @return the RegisteredClientEntity object with the specified client ID
     * @throws NullPointerException if clientId is null
     */
    @NotNull
    RegisteredClientEntity getRegisteredClientEntityByClientId(@NotNull String clientId);

    /**
     * Retrieves a slice of all registered client entities.
     *
     * This method retrieves a slice of all registered client entities in the system,
     * based on the provided pageable object. It returns a slice containing a subset of registered client entities.
     *
     * @param pageable The pageable object containing pagination and sorting information (must not be null).
     * @return A {@link Slice} of {@link RegisteredClientEntity} objects.
     * @throws NullPointerException if {@code pageable} is null.
     */
    @NotNull
    Slice<RegisteredClientEntity> getAllRegisteredClientEntities(@NotNull Pageable pageable);

    /**
     * Updates the registered client entity with the provided information.
     *
     * @param clientId The ID of the client to update (must not be null).
     * @param dto The DTO object containing the updated client's information (must not be null).
     */
    void updateRegisteredClientEntity(@NotNull String clientId, @NotNull RegisteredClientEntityDto dto);

    /**
     * Deletes a registered client entity with the specified client ID.
     *
     * @param clientId the client ID of the registered client entity to delete (must not be null).
     */
    void deleteRegisteredClientEntity(@NotNull String clientId);
}
