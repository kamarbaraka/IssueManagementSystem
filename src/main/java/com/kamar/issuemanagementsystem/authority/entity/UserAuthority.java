package com.kamar.issuemanagementsystem.authority.entity;

import com.kamar.issuemanagementsystem.user.entity.User;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * the user authority entity.
 * @author kamar baraka.*/

@Entity
@Data
public class UserAuthority implements GrantedAuthority {

    @Id
    @Column(name = "authority", nullable = false, unique = true)
    private String authority;

    /*@ManyToMany(mappedBy = "authorities", fetch = FetchType.EAGER)
    private final List<User> members = new ArrayList<>();*/

}
