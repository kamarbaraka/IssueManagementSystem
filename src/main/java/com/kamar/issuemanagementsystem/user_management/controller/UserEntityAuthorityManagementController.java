package com.kamar.issuemanagementsystem.user_management.controller;

import com.kamar.issuemanagementsystem.user_management.apis.UserEntityAuthorityManagementApi;
import com.kamar.issuemanagementsystem.user_management.dtos.UserEntityAuthorityDto;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntityAuthority;
import com.kamar.issuemanagementsystem.user_management.models.UserEntityAuthorityModel;
import com.kamar.issuemanagementsystem.user_management.service.UserEntityAuthorityManagementService;
import com.kamar.issuemanagementsystem.user_management.service.UserEntityAuthorityModelAssembler;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;


/**
 * This class is a REST controller that provides APIs for managing user entity authorities.
 *
 * The class implements the {@link UserEntityAuthorityManagementApi} interface and handles HTTP requests related to user entity authorities.
 *
 * The class uses the {@link UserEntityAuthorityManagementService} for managing the user entity authorities.
 *
 * The class also uses the {@link UserEntityAuthorityModelAssembler} for converting {@link UserEntityAuthority} objects to {@link UserEntityAuthorityModel} objects.
 *
 * The class is annotated with {@link RestController} to indicate that it is a controller and handles RESTful requests.
 * It is also annotated with {@link RequiredArgsConstructor} to automatically inject dependencies via constructor injection.
 * The class is mapped to the "/api/v1/authorities" URI using the {@link RequestMapping} annotation.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/authorities"})
public class UserEntityAuthorityManagementController implements UserEntityAuthorityManagementApi {

    private final UserEntityAuthorityManagementService authorityManagementService;
    private final UserEntityAuthorityModelAssembler assembler;
    private final CacheControl cacheControl;

    /**
     * Creates a user entity authority based on the provided UserEntityAuthorityDto.
     *
     * @param dto the UserEntityAuthorityDto object containing the authority value (must not be null)
     * @return a ResponseEntity object with a void response
     * @throws IllegalArgumentException if the dto parameter is null
     * @see ResponseEntity
     */
    @PostMapping
    @NotNull
    @Override
    public ResponseEntity<Void> createUserEntityAuthority(@NotNull @RequestBody UserEntityAuthorityDto dto) {

        /*create the entity*/
        authorityManagementService.createUserEntityAuthority(dto);

        URI authorityUri = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(UserEntityAuthorityManagementController.class)
                .getUserEntityAuthorityByAuthority(dto.authority())
        ).toUri();

        return ResponseEntity.created(authorityUri)
                .build();
    }

    /**
     * Retrieves the UserEntityAuthorityModel object based on the given authority.
     *
     * This method retrieves the UserEntityAuthority object from the authorityManagementService based on the provided authority.
     * It then converts the UserEntityAuthority object to a UserEntityAuthorityModel object using the assembler.
     * The method returns a ResponseEntity object containing the UserEntityAuthorityModel object.
     *
     * Usage:
     * ResponseEntity<UserEntityAuthorityModel> response = getUserEntityAuthorityByAuthority("ROLE_ADMIN");
     * UserEntityAuthorityModel model = response.getBody();
     *
     * @param authority the authority of the UserEntityAuthority object to retrieve (must not be null).
     * @return a ResponseEntity object containing the UserEntityAuthorityModel object.
     *
     * @throws IllegalArgumentException if the authority parameter is null.
     *
     * @see ResponseEntity
     * @see UserEntityAuthority
     * @see UserEntityAuthorityModel
     */
    @GetMapping
    @NotNull
    @Override
    public ResponseEntity<UserEntityAuthorityModel> getUserEntityAuthorityByAuthority(@NotNull @RequestParam("authority")
                                                                                          String authority) {

        /*get the entity*/
        UserEntityAuthority entity = authorityManagementService.getUserEntityAuthorityByAuthority(authority);
        UserEntityAuthorityModel model = assembler.toModel(entity);

        return ResponseEntity.ok()
                .cacheControl(cacheControl)
                .lastModified(entity.getUpdatedOn())
                .body(model);
    }

    /**
     * Retrieves a slice of UserEntityAuthorityModel objects representing all user entity authorities.
     *
     * This method retrieves all user entity authorities using the provided Pageable object, which specifies the page number, page size, and sorting criteria for the result.
     * The Pageable object must not be null.
     *
     * The method returns a ResponseEntity object containing a Slice of UserEntityAuthorityModel objects that represent the user entity authorities.
     *
     * Usage:
     * Pageable = PageRequest.of(0, 10, Sort.by("authority").ascending());
     * ResponseEntity<Slice<UserEntityAuthorityModel>> response = getAllUserEntityAuthorities(pageable);
     * Slice<UserEntityAuthorityModel> modelSlice = response.getBody();
     *
     * @param pageable the Pageable object specifying the page number, page size, and sorting criteria
     * @return a ResponseEntity object containing a Slice of UserEntityAuthorityModel objects representing the user entity authorities
     * @throws IllegalArgumentException if the pageable parameter is null
     *
     * @see Pageable
     * @see Slice
     * @see UserEntityAuthorityModel
     * @see ResponseEntity
     */
    @GetMapping
    @NotNull
    @Override
    public ResponseEntity<Slice<UserEntityAuthorityModel>> getAllUserEntityAuthorities(@NotNull @RequestBody Pageable pageable) {

        /*get all entities*/
        Slice<UserEntityAuthority> authoritySlice = authorityManagementService.getUserEntityAuthoritiesWithSlice(pageable);
        /*convert them to models*/
        Slice<UserEntityAuthorityModel> modelSlice = authoritySlice.map(assembler::toModel);

        /*respond*/
        return ResponseEntity.ok(modelSlice);

    }

    /**
     * Updates the authority of a user entity.
     *
     * <p>This method updates the authority of a user entity based on the provided authority. The authority is validated
     * against the given UserEntityAuthorityDto object. The authority value must not be null, empty, or blank. If the provided
     * authority does not match any existing user entity authority, an exception will be thrown.</p>
     *
     * @param authority the new authority value for the user entity
     * @param dto the data transfer object representing the user entity authority
     * @return a ResponseEntity object with a void response
     * @throws IllegalArgumentException if the provided authority is null, empty, or blank
     * @throws EntityNotFoundException if the provided authority does not match any existing user entity authority
     */
    @PatchMapping
    @NotNull
    @Override
    public ResponseEntity<Void> updateUserEntityAuthority(@NotNull @RequestParam("authority") String authority,
                                                          @NotNull @RequestBody UserEntityAuthorityDto dto) {

        /*update the authority*/
        authorityManagementService.updateUserEntityAuthority(authority, dto);

        /*respond*/
        return ResponseEntity.noContent()
                .build();
    }

    /**
     * Deletes a user entity authority based on the authority.
     *
     * @param authority the authority to delete (must not be null)
     * @return a ResponseEntity object with a void response
     * @throws IllegalArgumentException if the authority parameter is null
     */
    @DeleteMapping
    @NotNull
    @Override
    public ResponseEntity<Void> deleteUserEntityAuthority(@NotNull @RequestParam("authority") String authority) {

        /*delete the authority*/
        authorityManagementService.deleteUserEntityAuthorityByAuthority(authority);

        /*respond*/
        return ResponseEntity.noContent()
                .build();
    }
}
