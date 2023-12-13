package com.kamar.issuemanagementsystem.app_properties;

import jakarta.validation.constraints.Email;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * the init user property.
 * @author kamar baraka.*/

@ConfigurationProperties(prefix = "app.init")
public record InnitUserProperties(

        @Email
        String username,
        String password,
        String departmentName,
        @Email
        String departmentEmail,
        String dbUsername,
        String dbPassword,
        String dbUrl
) {
}
