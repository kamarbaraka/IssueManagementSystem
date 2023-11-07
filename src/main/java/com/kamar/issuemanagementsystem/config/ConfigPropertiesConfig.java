package com.kamar.issuemanagementsystem.config;

import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import com.kamar.issuemanagementsystem.app_properties.InnitUserProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * configuration for the application properties.
 * @author kamar baraka.*/

@Configuration
@EnableConfigurationProperties(value = {
        InnitUserProperties.class,
        CompanyProperties.class
})
public class ConfigPropertiesConfig {
}
