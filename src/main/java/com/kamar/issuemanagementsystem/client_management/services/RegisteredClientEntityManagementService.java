package com.kamar.issuemanagementsystem.client_management.services;

import com.kamar.issuemanagementsystem.client_management.dtos.RegisteredClientEntityDto;
import com.kamar.issuemanagementsystem.client_management.entities.RegisteredClientEntity;
import com.kamar.issuemanagementsystem.client_management.exceptions.ClientEntityReadException;
import com.kamar.issuemanagementsystem.client_management.models.RegisteredClientEntityModel;
import com.kamar.issuemanagementsystem.client_management.repositories.RegisteredClientEntityRepository;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.stereotype.Service;

import java.util.UUID;


/**
 * The RegisteredClientEntityManagementService class is responsible for managing registered client entities in the system.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Service
@RequiredArgsConstructor
public class RegisteredClientEntityManagementService implements RegisteredClientEntityManagerService{

    private final RegisteredClientEntityRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final RegisteredClientEntityModelAssembler assembler;
    public static final String  READ_ERROR = "No such resource!";

    /**
     * Registers a client entity with the provided data.
     *
     * This method builds an instance of {@link RegisteredClientEntity} using the given {@link RegisteredClientEntityDto}
     * and saves it to the repository. It then encodes the client secret and returns the corresponding
     * {@link RegisteredClientEntityModel} representing the registered client entity.
     *
     * @param dto The {@link RegisteredClientEntityDto} object containing the data for the client entity to be registered.
     *            Must not be null.
     * @return The {@link RegisteredClientEntityModel} representing the registered client entity.
     */
    @Override
    public RegisteredClientEntityModel registerClientEntity(@NotNull RegisteredClientEntityDto dto) {

        /*build the entity*/
        RegisteredClientEntity entity = RegisteredClientEntity.builder()
                .clientName(dto.clientName())
                .redirectUri(dto.redirectUri())
                .postLogoutRedirectUri(dto.postLogoutRedirectUri())
                .authorizationGrantTypes(dto.authorizationGrantTypes())
                .build();

        /*get the model*/
        RegisteredClientEntityModel model = assembler.toModel(entity);

        /*encode the secret*/
        entity.setClientSecret(passwordEncoder.encode(entity.getClientSecret()));

        /*persist*/
        repository.save(entity);

        /*return */
        return model;

    }

    /**
     * Regenerates the client secret for the specified client ID.
     *
     * This method generates a new client secret using UUID.randomUUID().toString(),
     * updates the client entity in the repository with the new client secret,
     * and returns the registered client entity model with the updated information.
     *
     * @param clientId The client ID of the registered client entity to regenerate the secret for. Must not be null.
     * @return The RegisteredClientEntityModel with the updated client secret.
     * @throws ClientEntityReadException If there is an error while reading the client entity or if the entity is not found.
     * @since 1.0
     */
    @NotNull
    @Override
    public RegisteredClientEntityModel regenerateClientSecret(@NotNull String clientId) {

        /*generate new client secret*/
        String newClientSecret = UUID.randomUUID().toString();

        /*get the client with the client ID*/
        RegisteredClientEntity entity = repository.findRegisteredClientEntityByClientId(clientId)
                .orElseThrow(
                        () -> new ClientEntityReadException(READ_ERROR)
                );

        /*get the model*/
        RegisteredClientEntityModel model = assembler.toModel(entity);

        /*update the client secret*/
        entity.setClientSecret(passwordEncoder.encode(newClientSecret));

        /*persist*/
        repository.save(entity);

        /*return*/
        return model;

    }

    /**
     * Retrieves the RegisteredClientEntity with the given clientId.
     *
     * @param clientId The clientId of the RegisteredClientEntity to retrieve (must not be null).
     * @return The RegisteredClientEntity if found.
     * @throws ClientEntityReadException If there is an error while reading the client entity or if the entity is not found.
     * @since 1.0
     */
    @NotNull
    @Override
    public RegisteredClientEntity getRegisteredClientEntityByClientId(@NotNull String clientId) {

        /*get the entity with the ID*/
        return repository.findRegisteredClientEntityByClientId(clientId)
                .orElseThrow(
                        () -> new ClientEntityReadException(READ_ERROR)
                );
    }

    /**
     * Retrieves a slice of all registered client entities based on the provided pageable object.
     *
     * @param pageable The pageable object specifying the page number, page size, and sorting criteria. Must not be null.
     * @return A slice containing the registered client entities.
     */
    @NotNull
    @Override
    public Slice<RegisteredClientEntity> getAllRegisteredClientEntities(@NotNull Pageable pageable) {

        /*get all clients*/
        return repository.findAllWithSlice(pageable);
    }

    /**
     * Updates a registered client entity with the specified client ID.
     *
     * @param clientId The client ID of the registered client entity to be updated (must not be null).
     * @param dto The RegisteredClientEntityDto object containing the updated fields (must not be null).
     * @throws ClientEntityReadException If there is an error while reading the client entity.
     */
    @Override
    public void updateRegisteredClientEntity(@NotNull String clientId, @NotNull RegisteredClientEntityDto dto) {

        /*get the entity and update*/
        RegisteredClientEntity entity = repository.findRegisteredClientEntityByClientId(clientId)
                .orElseThrow(
                        () -> new ClientEntityReadException(READ_ERROR)
                );
        entity.setClientName(dto.clientName());
        entity.setRedirectUri(dto.redirectUri());
        entity.setPostLogoutRedirectUri(dto.postLogoutRedirectUri());
        entity.setAuthorizationGrantTypes(dto.authorizationGrantTypes());
        repository.save(entity);
    }

    /**
     * Deletes a registered client entity with the specified client ID.
     *
     * @param clientId the client ID of the registered client entity to delete (must not be null).
     */
    @Override
    public void deleteRegisteredClientEntity(@NotNull String clientId) {

        /*fins the entity and delete*/
        repository.findRegisteredClientEntityByClientId(clientId)
                .ifPresent(repository::delete);
    }

    /**
     * Saves a RegisteredClient object.
     *
     * @param registeredClient The RegisteredClient object to be saved.
     */
    @Override
    public void save(RegisteredClient registeredClient) {

        /*save the entity*/
    }

    /**
     * Finds a RegisteredClient object by its ID.
     *
     * @param id The ID of the RegisteredClient to find.
     * @return The matching RegisteredClient object if found.
     * @throws ClientEntityReadException If there is an error while reading the client entity.
     */
    @Override
    public RegisteredClient findById(String id) {

        /*find the entity with the ID*/
        return repository.findById(id)
                .map(this::entityToRegisteredClient)
                .orElseThrow(
                        () -> new ClientEntityReadException(READ_ERROR)
                );
    }

    /**
     * Finds a RegisteredClient object by its client ID.
     *
     * @param clientId The client ID of the RegisteredClient to find.
     * @return The matching RegisteredClient object if found, or null if not found.
     * @throws ClientEntityReadException If there is an error while reading the client entity.
     */
    @Override
    public RegisteredClient findByClientId(String clientId) {

        /*find the entity with the client ID*/
        return repository.findRegisteredClientEntityByClientId(clientId)
                .map(this::entityToRegisteredClient)
                .orElseThrow(() -> new ClientEntityReadException(READ_ERROR));
    }

    /**
     * Converts a RegisteredClientEntity object to a RegisteredClient object.
     *
     * @param entity The RegisteredClientEntity object to be converted.
     * @return The converted RegisteredClient object.
     */
    @NotNull
    private RegisteredClient entityToRegisteredClient(@NotNull RegisteredClientEntity entity){

        /*build the registered client*/
        return RegisteredClient.withId(entity.getId())
                .clientName(entity.getClientName())
                .clientId(entity.getClientId())
                .clientSecret(entity.getClientSecret())
                .redirectUri(entity.getRedirectUri())
                .postLogoutRedirectUri(entity.getPostLogoutRedirectUri())
                .clientAuthenticationMethods(methods -> methods.addAll(entity.getClientAuthenticationMethods()))
                .authorizationGrantTypes(grants -> grants.addAll(entity.getAuthorizationGrantTypes()))
                .scopes(scopes -> scopes.addAll(entity.getScopes()))
                .clientIdIssuedAt(entity.getClientIssuedAt())
                .clientSettings(entity.getClientSettings())
                .tokenSettings(entity.getTokenSettings())
                .build();
    }
}
