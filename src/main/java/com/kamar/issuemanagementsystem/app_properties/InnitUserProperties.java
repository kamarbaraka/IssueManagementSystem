package com.kamar.issuemanagementsystem.app_properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * the init user property.
 * @author kamar baraka.*/

@ConfigurationProperties(prefix = "app.init")
public record InnitUserProperties(

        String username,
        String password,
        String departmentName
) {
}
