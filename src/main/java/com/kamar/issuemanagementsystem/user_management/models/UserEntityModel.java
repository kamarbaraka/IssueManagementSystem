package com.kamar.issuemanagementsystem.user_management.models;

import com.kamar.issuemanagementsystem.user_management.entity.ExtraProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.util.Set;



/**
 * UserModel represents a user model that extends RepresentationModel.
 * It contains the username, email, and extraProperties of a user.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserEntityModel extends RepresentationModel<UserEntityModel> {

    private String username;

    private String email;

    private Set<ExtraPropertyModel> extraProperties;
}
