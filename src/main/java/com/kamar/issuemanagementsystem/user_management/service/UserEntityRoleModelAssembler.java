package com.kamar.issuemanagementsystem.user_management.service;


import com.kamar.issuemanagementsystem.user_management.controller.UserEntityRoleManagementController;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntityRole;
import com.kamar.issuemanagementsystem.user_management.models.UserEntityRoleModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.IanaLinkRelations;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * UserEntityRoleModelAssembler is a class that implements the RepresentationModelAssembler interface. It is responsible for converting UserEntityRole entities to UserEntityRoleModel
 * representations.
 *
 * The toModel method converts a UserEntityRole entity to a UserEntityRoleModel. It receives a UserEntityRole entity as a parameter and returns a UserEntityRoleModel object. The method
 * builds the UserEntityRoleModel using the UserEntityRoleModelBuilder and sets the role and grantedAuthorities fields based on the corresponding fields in the entity.
 *
 * The toCollectionModel method converts a collection of UserEntityRole entities to a CollectionModel of UserEntityRoleModel. It receives a collection of UserEntityRole entities as
 * a parameter and returns a CollectionModel of UserEntityRoleModel. The method iterates over the entities, calls the toModel method for each entity, and adds the resulting UserEntity
 *RoleModel to a list. It then creates a CollectionModel of UserEntityRoleModel using the list of models.
 *
 * Example Usage:
 * UserEntityRole entity = new UserEntityRole();
 * // Set entity fields...
 *
 * UserEntityRoleModelAssembler assembler = new UserEntityRoleModelAssembler();
 *
 * UserEntityRoleModel model = assembler.toModel(entity);
 * CollectionModel<UserEntityRoleModel> collectionModel = assembler.toCollectionModel(entities);
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Service
public class UserEntityRoleModelAssembler implements RepresentationModelAssembler<UserEntityRole, UserEntityRoleModel> {
    /**
     * Converts a UserEntityRole entity to a UserEntityRoleModel.
     *
     * @param entity The UserEntityRole entity to be converted.
     * @return The converted UserEntityRoleModel.
     */
    @Override
    public UserEntityRoleModel toModel(UserEntityRole entity) {

        /*build the model and return*/
        UserEntityRoleModel model = UserEntityRoleModel.builder()
                .role(entity.getRole())
                .grantedAuthorities(entity.getGrantedAuthorities())
                .build();

        /*create links*/
        Link about = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserEntityRoleManagementController.class)
                        .getUserEntityRoleByRole(entity.getRole())
        ).withRel(IanaLinkRelations.ABOUT);

        Link allRoles = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserEntityRoleManagementController.class)
                        .getAllUserEntityRoles()
        ).withRel(IanaLinkRelations.COLLECTION);

        model.add(about, allRoles);
        return model;
    }

    /**
     * Converts a collection of UserEntityRole entities to a CollectionModel of UserEntityRoleModel.
     *
     * @param entities The collection of UserEntityRole entities.
     * @return The CollectionModel of UserEntityRoleModel.
     */
    @Override
    public CollectionModel<UserEntityRoleModel> toCollectionModel(Iterable<? extends UserEntityRole> entities) {

        /*build the collection model*/
        List<UserEntityRoleModel> models = new ArrayList<>();
        entities.forEach(entity -> models.add(toModel(entity)));
        return CollectionModel.of(models);
    }
}
