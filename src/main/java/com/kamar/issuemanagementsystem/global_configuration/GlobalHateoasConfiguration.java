package com.kamar.issuemanagementsystem.global_configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * hateoas configuration.
 * @author kamar baraka.*/

@Configuration
@EnableHypermediaSupport(type = EnableHypermediaSupport.HypermediaType.HAL)
@EnableTransactionManagement
public class GlobalHateoasConfiguration {
}
