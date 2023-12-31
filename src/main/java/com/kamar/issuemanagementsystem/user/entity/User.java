package com.kamar.issuemanagementsystem.user.entity;


import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.rating.entity.UserRating;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;

/**
 *  the user entity.
 *  @author kamar baraka.*/

@Component
@Entity(name = "users")
@Data
public class User implements Serializable, UserDetails {

    @Id
    @Column(name = "username", unique = true, nullable = false)
    @Email
    private  String username;

    @Column(nullable = false)
    private String password;

    @Transient
    private final Random random = new Random();

    private String activationToken = Integer.toString(random.nextInt(1000, 9999));

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "users_authorities",joinColumns = {@JoinColumn(name = "user")},
            inverseJoinColumns = {@JoinColumn(name = "authority")})
    private List<UserAuthority> authorities = new ArrayList<>();

    @OneToOne( cascade = CascadeType.ALL, optional = false, orphanRemoval = true, fetch = FetchType.EAGER)
    @JoinColumn(name = "rating")
    private final UserRating userRating = new UserRating();

    @Column(nullable = false, updatable = false)
    private final LocalDate createdOn = LocalDate.now();

    private boolean accountNonExpired = true;

    private boolean accountNonLocked = true;

    private boolean credentialsNonExpired = true;

    private boolean enabled = false;

}
