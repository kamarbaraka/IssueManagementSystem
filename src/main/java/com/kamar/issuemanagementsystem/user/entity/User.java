package com.kamar.issuemanagementsystem.user.entity;


import com.kamar.issuemanagementsystem.rating.entity.Rating;
import com.kamar.issuemanagementsystem.user.data.Authority;
import jakarta.annotation.Nonnull;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.lang.NonNull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 *  the user entity.
 *  @author kamar baraka.*/

@Component
@Entity(name = "users")
@Data
public class User implements Serializable, UserDetails {

    @Id
    @Column(unique = true, nullable = false)
    @Email
    private  String username;

    @Column(nullable = false)
    private String password;

    private String activationToken = UUID.randomUUID().toString();

    @Enumerated(EnumType.STRING)
    private Authority authority = Authority.USER;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn(name = "rating", columnDefinition = "VARCHAR(255)")
    private Rating rating = new Rating();

    @Column(nullable = false, updatable = false)
    private final LocalDate createdOn = LocalDate.now();

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = false;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        return List.of(this.authority);
    }
}
