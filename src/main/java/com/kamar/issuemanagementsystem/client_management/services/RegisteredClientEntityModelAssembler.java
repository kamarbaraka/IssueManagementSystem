package com.kamar.issuemanagementsystem.client_management.services;

import com.kamar.issuemanagementsystem.client_management.controllers.RegisteredClientManagementController;
import com.kamar.issuemanagementsystem.client_management.entities.RegisteredClientEntity;
import com.kamar.issuemanagementsystem.client_management.models.RegisteredClientEntityModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.SlicedModel;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.util.Objects;


/**
 * The RegisteredClientEntityModelAssembler class is responsible for converting a RegisteredClientEntity object
 * into a RegisteredClientEntityModel object. It extends the RepresentationModelAssemblerSupport class
 * and provides the toModel() method implementation.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Service
public class RegisteredClientEntityModelAssembler extends RepresentationModelAssemblerSupport<RegisteredClientEntity, RegisteredClientEntityModel> {
    public RegisteredClientEntityModelAssembler(Class<?> controllerClass, Class<RegisteredClientEntityModel> resourceType) {
        super(controllerClass, resourceType);
    }

    /**
     * Converts a RegisteredClientEntity object to a RegisteredClientEntityModel object.
     *
     * @param entity The RegisteredClientEntity object to be converted.
     * @return The converted RegisteredClientEntityModel object.
     */
    @Override
    public RegisteredClientEntityModel toModel(RegisteredClientEntity entity) {

        /*create a links*/
        Link selfLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RegisteredClientManagementController.class)
                .getRegisteredClientByClientId(entity.getClientId())
        ).withRel(IanaLinkRelations.SELF);

        Link generateClientSecretLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RegisteredClientManagementController.class)
                .regenerateClientSecret(entity.getClientId())
        ).withRel("generateClientSecret");

        Link deleteClientLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RegisteredClientManagementController.class)
                .deleteRegisteredClient(entity.getClientId())
        ).withRel("deleteClient");

        /*build the model*/
        RegisteredClientEntityModel model = RegisteredClientEntityModel.builder()
                .clientName(entity.getClientName())
                .clientId(entity.getClientId())
                .clientSecret(entity.getClientSecret())
                .build();

        /*add the links*/
        model.add(selfLink, generateClientSecretLink, deleteClientLink);

        /*return*/
        return model;
    }

    /**
     * Converts a Slice object to a SlicedModel object.
     *
     * @param slice The Slice object to be converted.
     * @return The converted SlicedModel object.
     */
    @NotNull
    public SlicedModel<? extends EntityModel<? >> sliceToSliceModel(@NotNull Slice<?> slice){

        /*create a sliced model metadata*/
        SlicedModel.SliceMetadata sliceMetadata = new SlicedModel.SliceMetadata(slice.getSize(), slice.getNumber());
        /*create a slice model*/
        SlicedModel<? extends EntityModel<?>> slicedModel = SlicedModel.wrap(slice, sliceMetadata);

        /*create next and previous links*/
        Link nextLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RegisteredClientManagementController.class)
                .getAllRegisteredClients(
                        Pageable.ofSize(((int) Objects.requireNonNull(slicedModel.getMetadata()).getSize() + 1))
                )
        ).withRel(IanaLinkRelations.NEXT);

        Link prevLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(RegisteredClientManagementController.class)
                .getAllRegisteredClients(
                        Pageable.ofSize(((int) Objects.requireNonNull(slicedModel.getMetadata()).getSize() - 1))
                )
        ).withRel(IanaLinkRelations.PREVIOUS);

        /*add the links*/
        slicedModel.add(nextLink, prevLink);


        return slicedModel;
    }
}
