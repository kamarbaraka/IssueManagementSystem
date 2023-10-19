package com.kamar.issuemanagementsystem.user.entity;


import com.kamar.issuemanagementsystem.user.data.Authority;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
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
@Entity
@Data
@Table(name = "users")
public class User implements Serializable, UserDetails {

    @Id
    @GeneratedValue(generator = "UUID")
    @Column(nullable = false, updatable = false)
    private  UUID userId;

    @Column(unique = true, nullable = false)
    @Email
    private  String username;

    @Column(nullable = false)
    private String password;

    private String activationToken;

    @Enumerated(EnumType.STRING)
    private Collection<Authority> authorities = List.of(Authority.USER);

    private long totalStars = 0;

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = false;

    @Column(nullable = false, updatable = false)
    private final LocalDate createdOn = LocalDate.now();

}
