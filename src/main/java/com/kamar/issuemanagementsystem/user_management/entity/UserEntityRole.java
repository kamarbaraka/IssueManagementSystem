package com.kamar.issuemanagementsystem.user_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * This class represents a user entity role.
 * It is used to store information about the role and the granted authorities associated with the role.
 *
 * The class is annotated with @Entity and @Table to map it to a corresponding database table.
 * It includes fields annotated with @Id and @Column to specify the primary key and the column properties.
 * The grantedAuthorities field is annotated with @ElementCollection and @Column to specify the collection type
 * and the column properties for storing the set of granted authorities.
 *
 * The class includes getter and setter methods for accessing and modifying the role and granted authorities.
 * It also includes builder, all-args constructor, and no-args constructor to support object creation and initialization.
 * The updatedOn field has a default value of the current instant, which can be overridden using the builder.
 *
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "user_roles")
public class UserEntityRole {

    @NotBlank(message = "role cannot be blank.")
    @NotEmpty(message = "role cannot be empty.")
    @NotNull(message = "role cannot be null.")
    @Id
    @Column(name = "role", nullable = false, unique = true)
    private String role;

    @NotNull(message = "granted authorities cannot be null.")
    @ElementCollection
    @Column(name = "granted_authority")
    @CollectionTable(name = "user_roles_grantedAuthorities", joinColumns = @JoinColumn(name = "owner_id"))
    private Set<GrantedAuthority> grantedAuthorities = new LinkedHashSet<>();

    @NotNull(message = "updatedOn cannot be null.")
    @Builder.Default
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn = Instant.now();

}