package com.kamar.issuemanagementsystem.csrf.repository;

import com.kamar.issuemanagementsystem.csrf.entity.AppCsrfToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * repository for the app csrf token entity.
 * @author kamar baraka.*/

@Repository
public interface AppCsrfTokenRepository extends JpaRepository<AppCsrfToken, Long> {

    Optional<AppCsrfToken> findAppCsrfTokenByAppId(String appId);
}
