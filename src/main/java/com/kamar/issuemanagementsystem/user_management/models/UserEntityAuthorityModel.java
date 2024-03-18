package com.kamar.issuemanagementsystem.user_management.models;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.hateoas.RepresentationModel;


/**
 * Represents a user entity authority model.
 *
 * This class extends the {@link RepresentationModel} class and provides a representation of a user's authority.
 *
 * The UserEntityAuthorityModel class has a single attribute, authority, which represents the authority of the user.
 *
 * This class is annotated with the {@link AllArgsConstructor}, {@link Getter}, and {@link EqualsAndHashCode} annotations to
 * automatically generate a constructor, getters, and equals/hashCode methods based on the authority attribute.
 *
 * Usage:
 * UserEntityAuthorityModel userAuthority = new UserEntityAuthorityModel("ROLE_ADMIN");
 * String authority = userAuthority.getAuthority();
 *
 * @see RepresentationModel
 * @see AllArgsConstructor
 * @see Getter
 * @see EqualsAndHashCode
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@AllArgsConstructor
@Getter
@EqualsAndHashCode(callSuper = true)
public class UserEntityAuthorityModel extends RepresentationModel<UserEntityAuthorityModel> {

    private String authority;
}
