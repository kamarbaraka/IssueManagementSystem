package com.kamar.issuemanagementsystem.client_management.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;


/**
 * Represents a registered client entity model.
 *
 * This class extends the {@link RepresentationModel} class and provides
 * a representation model for a registered client entity. It contains the following attributes:
 *
 * - {@code clientName}: A String representing the client name.
 * - {@code clientId}: A String representing the client ID.
 * - {@code clientSecret}: A String representing the client secret.
 *
 * This class uses the Lombok annotations to generate builder, constructor, equals and hashCode, and
 * getter methods automatically.
 *
 * @see RepresentationModel
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Getter
public class RegisteredClientEntityModel extends RepresentationModel<RegisteredClientEntityModel> {


    private String clientName;

    private String clientId;

    private String clientSecret;
}
