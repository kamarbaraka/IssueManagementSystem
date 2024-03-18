package com.kamar.issuemanagementsystem.client_management.apis;

import com.kamar.issuemanagementsystem.client_management.dtos.RegisteredClientEntityDto;
import com.kamar.issuemanagementsystem.client_management.models.RegisteredClientEntityModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.SlicedModel;
import org.springframework.http.ResponseEntity;

/**
 * The RegisteredClientManagementApi interface provides methods for managing registered clients.
 * It allows clients to be registered, retrieved, updated, and deleted.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
public interface RegisteredClientManagementApi {

    /**
     * Registers a new client with the provided client information.
     *
     * @param dto The RegisteredClientEntityDto object containing the client information. Cannot be null.
     * @return A ResponseEntity containing the RegisteredClientEntityModel if the client is successfully registered.
     *         The ResponseEntity will have a status code indicating the success or failure of the registration.
     *         The ResponseEntity will have a status code HttpStatus.CREATED if the registration is successful.
     *         Otherwise, it will have a status code indicating the error.
     * @throws IllegalArgumentException if the dto is null.
     */
    @NotNull
    ResponseEntity<RegisteredClientEntityModel> registerClient(@NotNull RegisteredClientEntityDto dto);

    /**
     * Retrieves a registered client by its client ID.
     *
     * @param clientId The client ID of the registered client. Cannot be null.
     * @return A ResponseEntity containing the RegisteredClientEntityModel if the client is found.
     *         The ResponseEntity will have a status code indicating the success or failure of the retrieval.
     *         The ResponseEntity will have a status code HttpStatus. OK if the retrieval is successful.
     *         Otherwise, it will have a status code indicating the error.
     * @throws IllegalArgumentException if the clientId is null.
     */
    @NotNull
    ResponseEntity<RegisteredClientEntityModel> getRegisteredClientByClientId(@NotNull String clientId);

    /**
     * Regenerates the client secret for a registered client identified by the specified client ID.
     *
     * @param clientId The client ID of the registered client. Cannot be null.
     * @return A ResponseEntity containing the RegisteredClientEntityModel with the regenerated client secret
     *         if the client is found and the secret is successfully regenerated.
     *         The ResponseEntity will have a status code HttpStatus.OK if the regeneration is successful.
     *         Otherwise, it will have a status code indicating the error.
     * @throws IllegalArgumentException if the clientId is null.
     */
    @NotNull
    ResponseEntity<RegisteredClientEntityModel> regenerateClientSecret(@NotNull String clientId);

    /**
     * Retrieves all registered clients.
     *
     * @param pageable The Pageable object used for pagination. Cannot be null.
     * @return A ResponseEntity containing a SlicedModel of RegisteredClientEntityModel if the clients are successfully retrieved.
     *         The ResponseEntity will have a status code indicating the success or failure of the retrieval.
     *         The ResponseEntity will have a status code HttpStatus.OK if the retrieval is successful.
     *         Otherwise, it will have a status code indicating the error.
     * @throws IllegalArgumentException if the pageable is null.
     */
    @NotNull
    ResponseEntity<SlicedModel<? extends EntityModel<? >>> getAllRegisteredClients(@NotNull Pageable pageable);

    /**
     * Updates a registered client with the provided client ID and DTO information.
     *
     * @param clientId The client ID of the registered client to update. Cannot be null.
     * @param dto The RegisteredClientEntityDto object containing the updated information for the registered client. Cannot be null.
     * @return A ResponseEntity with nobody and a status code indicating the success or failure of the update.
     *         The ResponseEntity will be {@link org.springframework.http.HttpStatus#OK} if the update is successful.
     *         Otherwise, it will have a status code indicating the error.
     * @throws IllegalArgumentException if the clientId or dto is null.
     */
    @NotNull
    ResponseEntity<Void> updateRegisteredClient(@NotNull String clientId, @NotNull RegisteredClientEntityDto dto);

    /**
     * Deletes a registered client by its client ID.
     *
     * @param clientId The client ID of the registered client to delete. Cannot be null.
     * @return A ResponseEntity with nobody and a status code indicating the success or failure of the deletion.
     *         The ResponseEntity will be {@link org.springframework.http.HttpStatus#NO_CONTENT} if the deletion is successful.
     *         Otherwise, it will have a status code indicating the error.
     * @throws IllegalArgumentException if the clientId is null.
     */
    @NotNull
    ResponseEntity<Void> deleteRegisteredClient(@NotNull String clientId);
}
