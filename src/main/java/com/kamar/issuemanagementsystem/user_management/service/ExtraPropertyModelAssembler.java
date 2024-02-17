package com.kamar.issuemanagementsystem.user_management.service;

import com.kamar.issuemanagementsystem.user_management.entity.ExtraProperty;
import com.kamar.issuemanagementsystem.user_management.models.ExtraPropertyModel;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

import java.util.ArrayList;


/**
 * The ExtraPropertyModelAssembler class is responsible for converting ExtraProperty entities
 * to their corresponding models, ExtraPropertyModel. It implements the RepresentationModelAssembler
 * interface from the Spring HATEOAS library.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Service
public class ExtraPropertyModelAssembler implements RepresentationModelAssembler<ExtraProperty, ExtraPropertyModel> {
    /**
     * Converts an ExtraProperty entity to its corresponding model, ExtraPropertyModel.
     *
     * @param entity The ExtraProperty entity to be converted.
     * @return The converted ExtraPropertyModel.
     */
    @Override
    public ExtraPropertyModel toModel(ExtraProperty entity) {

        /*build the model and return*/
        return ExtraPropertyModel.builder()
                .key(entity.getKey())
                .value(entity.getValue())
                .build();
    }

    /**
     * Converts a collection of ExtraProperty entities to a CollectionModel of ExtraPropertyModel entities.
     *
     * @param entities The collection of ExtraProperty entities to be converted.
     * @return The converted CollectionModel of ExtraPropertyModel entities.
     */
    @Override
    public CollectionModel<ExtraPropertyModel> toCollectionModel(Iterable<? extends ExtraProperty> entities) {

        /*create the collection model and return*/
        ArrayList<ExtraPropertyModel> extraPropertyModels = new ArrayList<>();
        entities.forEach(entity -> extraPropertyModels.add(toModel(entity)));
        return CollectionModel.of(extraPropertyModels);
    }
}
