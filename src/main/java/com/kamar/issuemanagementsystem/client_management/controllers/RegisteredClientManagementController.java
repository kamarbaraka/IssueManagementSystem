package com.kamar.issuemanagementsystem.client_management.controllers;


import com.kamar.issuemanagementsystem.client_management.apis.RegisteredClientManagementApi;
import com.kamar.issuemanagementsystem.client_management.dtos.RegisteredClientEntityDto;
import com.kamar.issuemanagementsystem.client_management.entities.RegisteredClientEntity;
import com.kamar.issuemanagementsystem.client_management.models.RegisteredClientEntityModel;
import com.kamar.issuemanagementsystem.client_management.services.RegisteredClientEntityManagerService;
import com.kamar.issuemanagementsystem.client_management.services.RegisteredClientEntityModelAssembler;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.*;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


/**
 * The RegisteredClientManagementController class is responsible for managing registered clients.
 * It provides methods for registering, retrieving, updating, and deleting clients.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@RestController
@RequestMapping(value = {"api/v1/OAuth2Clients"})
@RequiredArgsConstructor
public class RegisteredClientManagementController implements RegisteredClientManagementApi {

    private final RegisteredClientEntityManagerService clientEntityManagerService;
    private final RegisteredClientEntityModelAssembler assembler;
    private final CacheControl cacheControl;

    /**
     * Registers a client.
     *
     * This method takes a {@link RegisteredClientEntityDto} object containing the client information and registers
     * the client in the system. It returns a {@link ResponseEntity} object with the {@link RegisteredClientEntityModel}
     * representing the registered client.
     *
     * @param dto The {@link RegisteredClientEntityDto} object containing the client information (must not be null).
     * @return A {@link ResponseEntity} object with the {@link RegisteredClientEntityModel} representing the registered client.
     */
    @PatchMapping
    @NotNull
    @Override
    public ResponseEntity<RegisteredClientEntityModel> registerClient(@NotNull @RequestBody RegisteredClientEntityDto dto) {

        /*register the client*/
        RegisteredClientEntityModel model = clientEntityManagerService.registerClientEntity(dto);


        /*respond*/
        return ResponseEntity.created(model.getRequiredLink(IanaLinkRelations.SELF).toUri())
                .body(model);

    }

    /**
     * Retrieves a registered client by its client ID.
     *
     * This method retrieves the registered client entity with the specified client ID.
     * It returns a ResponseEntity object with the RegisteredClientEntityModel representing the registered client entity.
     *
     * @param clientId The client ID for which to retrieve the registered client (must not be null).
     * @return A ResponseEntity object with the RegisteredClientEntityModel representing the registered client entity.
     * @throws NullPointerException if clientId is null.
     */
    @GetMapping
    @NotNull
    @Override
    public ResponseEntity<RegisteredClientEntityModel> getRegisteredClientByClientId(@NotNull @RequestParam("clientId") String clientId) {

        /*get the entity with the ID*/
        RegisteredClientEntity entity = clientEntityManagerService.getRegisteredClientEntityByClientId(clientId);
        /*get the model*/
        RegisteredClientEntityModel model = assembler.toModel(entity);
        /*respond*/
        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .lastModified(entity.getUpdatedOn())
                .body(model);
    }

    /**
     * Regenerates the client secret for the specified client ID.
     *
     * This method takes a client ID as input, generates a new client secret, and updates the client entity
     * in the system with the new client secret. It returns a RegisteredClientEntityModel object representing
     * the updated client entity.
     *
     * @param clientId The client ID for which to regenerate the client secret (must not be null).
     * @return A ResponseEntity with the RegisteredClientEntityModel representing the updated client entity.
     * @throws NullPointerException if clientId is null.
     */
    @PatchMapping
    @NotNull
    @Override
    public ResponseEntity<RegisteredClientEntityModel> regenerateClientSecret(@NotNull @RequestParam("clientId") String clientId) {

        /*regenerate client secret*/
        RegisteredClientEntityModel model = clientEntityManagerService.regenerateClientSecret(clientId);

        /*respond*/
        return ResponseEntity.ok()
                .body(model);
    }

    /**
     * Retrieves a slice of all registered clients.
     *
     * This method retrieves a slice of all registered clients in the system,
     * based on the provided pageable object. It returns a slice containing a subset of registered clients.
     *
     * @param pageable The pageable object containing pagination and sorting information (must not be null).
     * @return A ResponseEntity with the sliced model of registered clients.
     * @throws NullPointerException if {@code pageable} is null.
     */
    @GetMapping
    @NotNull
    @Override
    public ResponseEntity<SlicedModel<? extends EntityModel<? >>> getAllRegisteredClients(@NotNull @RequestBody Pageable pageable) {

        /*get all clients and convert them to models*/
        Slice<RegisteredClientEntityModel> modelSlice = clientEntityManagerService.getAllRegisteredClientEntities(pageable)
                .map(assembler::toModel);

        SlicedModel<? extends EntityModel<?>> entityModels = assembler.sliceToSliceModel(modelSlice);

        return ResponseEntity.ok(entityModels);
    }

    /**
     * Updates a registered client with the provided client ID and DTO information.
     *
     * @param clientId The client ID of the registered client to update (must not be null).
     * @param dto The RegisteredClientEntityDto object containing the updated information for the registered client (must not be null).
     * @return A ResponseEntity with no content.
     */
    @PatchMapping
    @NotNull
    @Override
    public ResponseEntity<Void> updateRegisteredClient(@NotNull @RequestParam("clientId") String clientId,
                                                       @NotNull @RequestBody RegisteredClientEntityDto dto) {

        /*update the registered client*/
        clientEntityManagerService.updateRegisteredClientEntity(clientId, dto);
        return ResponseEntity.noContent().build();
    }

    /**
     * Deletes a registered client with the specified client ID.
     *
     * @param clientId the client ID of the registered client to delete (must not be null).
     * @return a ResponseEntity with no content.
     */
    @DeleteMapping
    @NotNull
    @Override
    public ResponseEntity<Void> deleteRegisteredClient(@NotNull @RequestParam("clientId") String clientId) {

        /*delete the client*/
        clientEntityManagerService.deleteRegisteredClientEntity(clientId);
        return ResponseEntity.noContent().build();
    }
}
