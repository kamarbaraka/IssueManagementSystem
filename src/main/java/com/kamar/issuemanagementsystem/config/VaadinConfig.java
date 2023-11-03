package com.kamar.issuemanagementsystem.config;

import com.vaadin.flow.spring.annotation.EnableVaadin;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * vaadin configuration class.
 * @author kamar baraka.*/

@Configuration
@EnableVaadin
//@PWA(name = "Ticket Management", shortName = "TMS")
public class VaadinConfig   {
}
