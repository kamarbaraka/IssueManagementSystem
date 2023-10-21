package com.kamar.issuemanagementsystem.ticket.controller;

import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketCreationDTO;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.service.TicketCreationService;
import com.kamar.issuemanagementsystem.ticket.service.TicketManagementService;
import com.kamar.issuemanagementsystem.ticket.utility.mapper.TicketMapper;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import com.kamar.issuemanagementsystem.user.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * the controller for ticket creation.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/api/v1/tickets"})
public class TicketCreationController {

    private final TicketMapper ticketMapper;
    private final UserManagementService userManagementService;
    private final TicketCreationService ticketCreationService;
    private final TicketManagementService ticketManagementService;


    /**
     * create a {@link }*/
    @PostMapping
    @Operation(tags = {"Ticket Creation"}, summary = "create a ticket", description = "use this api to raise a ticket")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EntityModel<DtoType>> createTicket(@AuthenticationPrincipal UserDetails userDetails,
                                                            @RequestBody TicketCreationDTO ticketCreationDTO){

        /*map the dto to entity*/
        Ticket raisedTicket = ticketMapper.dtoToEntity(ticketCreationDTO);
        /*set the necessary properties*/
        raisedTicket.setRaisedBy(userManagementService.getUserByUsername(userDetails.getUsername()));
        /*create the user*/
        Ticket savedTicket = ticketCreationService.createTicket(raisedTicket);
        /*construct a response*/
        DtoType info = new InfoDTO("ticket successfully created");
        EntityModel<DtoType> response = EntityModel.of(info);
        /*create links*/
        Link linkToTicket = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TicketCreationController.class).getTicketById(
                        savedTicket.getTicketId())).withRel("ticket");

        response.add(linkToTicket);

        /*return the response*/
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }

    @GetMapping(value = {"{id}"}, produces = {MediaType.APPLICATION_GRAPHQL_RESPONSE_VALUE})
    @Operation(tags = {"Ticket Creation", "Ticket Assignment", "Ticket Submission"},
            summary = "get a ticket", description = "get a ticket by ID")
    @PreAuthorize("permitAll()")
    public ResponseEntity<EntityModel<DtoType>> getTicketById(@PathVariable("id") long id){

        /*get the ticket*/
        Ticket ticket = ticketManagementService.getTicketById(id);
        /*map the ticket*/
        DtoType adminDto = ticketMapper.entityToDTOAdmin(ticket);
        /*construct the response*/
        EntityModel<DtoType> response = EntityModel.of(adminDto);
        /*add links*/

        /*return response*/
        return ResponseEntity.ok(response);
    }
}
