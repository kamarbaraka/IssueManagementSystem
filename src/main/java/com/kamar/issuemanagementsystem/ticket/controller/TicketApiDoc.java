package com.kamar.issuemanagementsystem.ticket.controller;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

/**
 * the Api definition of the ticket controller.
 * @author kamar baraka.*/


@RestController
@OpenAPIDefinition(tags = {@Tag(name = "Ticket Creation", description = "List of APIs for ticket creation"),
@Tag(name = "Ticket Assignment", description = "List of Apis for ticket assignment"),
@Tag(name = "Ticket Submission", description = "Apis for ticket submission")}, info =
@Info(title = "Ticket API",description = "Documentation for the ticket apis",version = "1.0.0.1", contact =
@Contact(name = "kamar baraka", email = "kamar254baraka@gmail.com")))
public class TicketApiDoc {
}
