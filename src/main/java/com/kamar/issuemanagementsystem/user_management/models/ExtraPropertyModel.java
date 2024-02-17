package com.kamar.issuemanagementsystem.user_management.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;


/**
 * A model class representing an extra property.
 *
 * This class extends the RepresentationModel from Spring HATEOAS library, which is used to add
 * hypermedia links to a model representation.
 *
 * This class is annotated with @Builder from lombok library, which helps in generating a builder
 * pattern for this class.
 *
 * This class is annotated with @AllArgsConstructor from lombok library, which generates a
 * constructor with all required arguments.
 *
 * This class is annotated with @EqualsAndHashCode(callSuper = true) from lombok library, which
 * generates equals and hashCode methods, including the ones inherited from the superclass.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class ExtraPropertyModel extends RepresentationModel<ExtraPropertyModel> {

    private String key;
    private Object value;
}
