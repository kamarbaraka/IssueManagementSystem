package com.kamar.issuemanagementsystem.user_management.service;

import com.kamar.issuemanagementsystem.user_management.entity.UserEntityAuthority;
import com.kamar.issuemanagementsystem.user_management.models.UserEntityAuthorityModel;
import jakarta.validation.constraints.NotNull;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Service;

/**
 * Converts a UserEntityAuthority object to a UserEntityAuthorityModel object.
 *
 * This class implements the RepresentationModelAssembler interface and provides a method to convert
 * a UserEntityAuthority object to a UserEntityAuthorityModel object.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */

@Service
public class UserEntityAuthorityModelAssembler implements RepresentationModelAssembler<UserEntityAuthority, UserEntityAuthorityModel> {

    /**
     * Converts a UserEntityAuthority object to a UserEntityAuthorityModel object.
     *
     * @param entity The UserEntityAuthority object to be converted.
     * @return The converted UserEntityAuthorityModel object.
     *
     * @throws NullPointerException if the entity parameter is null.
     */
    @NotNull
    @Override
    public UserEntityAuthorityModel toModel(@NotNull UserEntityAuthority entity) {

        /* set the model*/
        UserEntityAuthorityModel model = new UserEntityAuthorityModel(entity.getAuthority());

        /*links*/

        return model;
    }
}
