package com.kamar.issuemanagementsystem.authority.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

/**
 * the user authority entity.
 * @author kamar baraka.*/

@Entity
@Data
public class UserAuthority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "authority", nullable = false)
    private String authority = "USER";

    public static UserAuthority getFor(String authority){

        UserAuthority userAuthority = new UserAuthority();
        userAuthority.setAuthority(authority.toUpperCase());
        return userAuthority;
    }
}
