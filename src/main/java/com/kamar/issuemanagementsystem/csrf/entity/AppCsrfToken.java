package com.kamar.issuemanagementsystem.csrf.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * the csrf protection token entity.
 * @author kamar baraka.*/

@Entity(name = "app_csrf_token")
@Data
public class AppCsrfToken {

    @Id
    @Column(nullable = false, name = "app_id", unique = true)
    private String appId;

    @Column(nullable = false, name = "token")
    private String token;

    @Column(name = "expired")
    private boolean expired;

}
