package com.kamar.issuemanagementsystem.app_properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * company configuration.
 * @author kamar baraka.*/

@ConfigurationProperties(prefix = "app.company")
public record CompanyProperties(

        String name,
        String email,
        String endTag
) {
}
