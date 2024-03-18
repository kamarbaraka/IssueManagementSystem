package com.kamar.issuemanagementsystem.user_management.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;

import java.time.Instant;


/**
 * Represents a user authority entity.
 *
 * The UserEntityAuthority class models a user authority entity and implements the GrantedAuthority
 * interface. It stores information about the authority of a user.
 *
 * This class provides methods to get and set the ID, authority name, and update timestamp of the user authority.
 * It also includes annotations for mapping the class to a database table.
 *
 * The class includes the following annotations:
 * - @Entity: Specifies that this class is an entity and maps it to a database table.
 * - @Table: Specifies the name of the database table.
 *
 * This class also includes the following annotations for field validation and mapping:
 * - @Id: Specifies that the annotated field is a primary key.
 * - @GeneratedValue: Specifies the strategy for generating the ID value.
 * - @Column: Specifies the column name, constraints, and options for the annotated field.
 * - @NotBlank: Specifies that the annotated field must not be blank.
 * - @NotEmpty: Specifies that the annotated field must not be empty.
 * - @NotNull: Specifies that the annotated field must not be null.
 * - @Builder: Specifies that this class supports the builder design pattern for object creation.
 * - @Getter: Generates getter methods for the annotated fields.
 * - @Setter: Generates setter methods for the annotated fields.
 * - @NoArgsConstructor: Generates a no-argument constructor.
 * - @AllArgsConstructor: Generates a constructor with all arguments.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "user_authorities")
public class UserEntityAuthority implements GrantedAuthority {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotBlank(message = "authority cannot be blank.")
    @NotEmpty(message = "authority cannot be empty.")
    @NotNull(message = "authority cannot be null.")
    @Column(name = "authority", nullable = false, unique = true)
    private String authority;

    @NotNull(message = "updatedOn cannot be null.")
    @Builder.Default
    @Column(name = "updated_on", nullable = false)
    private Instant updatedOn = Instant.now();

}