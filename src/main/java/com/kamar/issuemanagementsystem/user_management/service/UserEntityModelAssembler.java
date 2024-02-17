package com.kamar.issuemanagementsystem.user_management.service;

import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import com.kamar.issuemanagementsystem.user_management.models.UserEntityModel;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * The UserEntityModelAssembler class is responsible for converting UserEntity entities to their corresponding
 * UserEntityModel models. It implements the RepresentationModelAssembler interface from the Spring HATEOAS library.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Service
@RequiredArgsConstructor
public class UserEntityModelAssembler implements RepresentationModelAssembler<UserEntity, UserEntityModel> {

    private final ExtraPropertyModelAssembler extraPropAssembler;

    /**
     * Converts a UserEntity to its corresponding UserEntityModel.
     *
     * @param entity The UserEntity to be converted.
     * @return The converted UserEntityModel.
     */
    @Override
    public UserEntityModel toModel(UserEntity entity) {

        /*Build the model and return*/
        return UserEntityModel.builder()
                .username(entity.getUsername())
                .email(entity.getEmail())
                .extraProperties(entity.getExtraProperties().stream()
                        .map(extraPropAssembler::toModel).collect(Collectors.toSet()))
                .build();
    }

    /**
     * Converts a collection of UserEntity entities to a CollectionModel of UserEntityModel entities.
     *
     * @param entities The collection of UserEntity entities to be converted.
     * @return The converted CollectionModel of UserEntityModel entities.
     */
    @Override
    public CollectionModel<UserEntityModel> toCollectionModel(Iterable<? extends UserEntity> entities) {

        /*create a collection model and return*/
        List<UserEntityModel> userEntityModels = new ArrayList<>();
        entities.forEach(entity -> userEntityModels.add(toModel(entity)));
        return CollectionModel.of(userEntityModels);
    }
}
