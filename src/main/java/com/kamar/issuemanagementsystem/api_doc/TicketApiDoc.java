package com.kamar.issuemanagementsystem.api_doc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

/**
 * the Api definition of the ticket controller.
 * @author kamar baraka.*/


@RestController
@OpenAPIDefinition(
        tags = {
                @Tag(name = "Ticket Creation", description = "List of APIs for ticket creation"),
                @Tag(name = "Ticket Assignment", description = "List of Apis for ticket assignment"),
                @Tag(name = "Ticket Submission", description = "Apis for ticket submission"),
                @Tag(name = "Ticket Reporting", description = "Apis to report on tickets"),
                @Tag(name = "Ticket Analysis", description = "Apis to analyse tickets"),
                @Tag(name = "Ticket Management", description = "Apis to manage a ticket"),
                @Tag(name = "User Registration", description = "Apis to register a user"),
                @Tag(name = "User Activation", description = "Apis to activate a user"),
                @Tag(name = "User Management", description = "Apis to manage a user"),
                @Tag(name = "User Analysis", description = "Apis for user analysis")
        },
        info = @Info(
                title = "Ticket Management System Api Documentation",
                description = "The documentation for the ticket management system Apis",
                version = "1.0.0.1",
                contact = @Contact(
                        name = "kamar baraka",
                        email = "kamar254baraka@gmail.com")
        )
)
public class TicketApiDoc {
}
