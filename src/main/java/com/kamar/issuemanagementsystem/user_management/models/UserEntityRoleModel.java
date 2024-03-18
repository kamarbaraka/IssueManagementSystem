package com.kamar.issuemanagementsystem.user_management.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.security.core.GrantedAuthority;

import java.util.Set;


/**
 * Represents a user entity role model.
 *
 * <p>
 * This class represents a user entity role model and extends the {@link RepresentationModel} class.
 * It contains information about the role and granted authorities for a user entity.
 * </p>
 *
 * <p>
 * This class is created with the help of the Lombok annotations - {@link Builder}, {@link AllArgsConstructor},
 * and {@link EqualsAndHashCode}. These annotations minimize boilerplate code such as constructors,
 * equals and hashcode methods, etc.
 * </p>
 *
 * <p>
 * This class does not contain any specific methods. It only maintains the state of the user entity role model
 * using the private fields - {@code role} and {@code grantedAuthorities}.
 * </p>
 *
 * <p>
 * Example Usage:
 * </p>
 * <pre>
 * UserEntityRoleModel userRole = UserEntityRoleModel.builder()
 *                             .role("ROLE_USER")
 *                             .grantedAuthorities(authorities)
 *                             .build();
 * </pre>
 *
 * @see RepresentationModel
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class UserEntityRoleModel extends RepresentationModel<UserEntityRoleModel> {

    private String role;

    private Set<GrantedAuthority> grantedAuthorities;
}
