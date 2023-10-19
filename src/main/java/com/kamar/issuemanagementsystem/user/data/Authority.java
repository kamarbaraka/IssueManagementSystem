package com.kamar.issuemanagementsystem.user.data;

import org.springframework.security.core.GrantedAuthority;

import java.io.Serializable;

/**
 * the user authorities.
 * @author kamar baraka.*/


public enum Authority implements GrantedAuthority, Serializable {

    ADMIN("ADMIN"),
    USER("USER"),
    EMPLOYEE("EMPLOYEE"),
    ANONYMOUS("ANONYMOUS"),
    ;
    private final String authority;

    Authority(String authority) {

        this.authority = authority;
    }

    @Override
    public String getAuthority() {
        return this.authority;
    }
}
