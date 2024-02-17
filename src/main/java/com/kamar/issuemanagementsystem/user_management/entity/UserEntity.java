package com.kamar.issuemanagementsystem.user_management.entity;


import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.*;

/**
 * Represents a User entity that implements the UserDetails interface.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */

@Entity(name = "users")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity implements UserDetails {

    @Transient
    private final Random random = new Random();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long userId;

    @NotBlank(message = "username cannot be blank.")
    @NotEmpty(message = "username cannot be empty.")
    @NotNull(message = "username cannot be null.")
    @Column(unique = true)
    private String username;

    @NotBlank(message = "email cannot be blank.")
    @NotEmpty(message = "email cannot be empty.")
    @NotNull(message = "email cannot be null.")
    @Column(name = "email", unique = true, nullable = false)
    @Email(message = "value must be a valid email.")
    private  String email;

    @NotBlank(message = "password cannot be blank.")
    @NotEmpty(message = "password vannot be empty.")
    @NotNull(message = "password cannot be null.")
    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "userEntity", cascade = {CascadeType.PERSIST, CascadeType.MERGE, CascadeType.REFRESH,
            CascadeType.DETACH}, orphanRemoval = true)
    private Set<ExtraProperty> extraProperties = new LinkedHashSet<>();

    @Builder.Default
    private String activationToken = Integer.toString(random.nextInt(1000, 9999));

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "users_authorities",joinColumns = {@JoinColumn(name = "user")},
            inverseJoinColumns = {@JoinColumn(name = "authority")})
    private Set<UserAuthority> authorities = new LinkedHashSet<>();

    @Column(nullable = false, updatable = false)
    private final Instant createdOn = Instant.now();

    @Builder.Default
    @Column(nullable = false)
    private Instant updatedOn = Instant.now();

    @Builder.Default
    private boolean accountNonExpired = true;

    @Builder.Default
    private boolean accountNonLocked = true;

    @Builder.Default
    private boolean credentialsNonExpired = true;

    @Builder.Default
    private boolean enabled = false;


}
