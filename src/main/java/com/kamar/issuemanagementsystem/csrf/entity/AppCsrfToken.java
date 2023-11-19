package com.kamar.issuemanagementsystem.csrf.entity;

import jakarta.persistence.*;
import lombok.Data;

/**
 * the csrf protection token entity.
 * @author kamar baraka.*/

@Entity(name = "app_csrf_token")
@Data
public class AppCsrfToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, updatable = false, name = "id")
    private long id;

    @Column(nullable = false, name = "app_id")
    private String appId;

    @Column(nullable = false, name = "token")
    private String token;

    @Column(name = "expired")
    private boolean expired;
}
