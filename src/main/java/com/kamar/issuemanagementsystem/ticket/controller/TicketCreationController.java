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
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
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
    @Operation(tags = {"Ticket Creation"}, summary = "create a ticket", description = "use this api to raise a ticket",
    security = {@SecurityRequirement(name = "USER"), @SecurityRequirement(name = "ADMIN"), @SecurityRequirement(name = "EMPLOYEE")})
    @PreAuthorize("hasAnyAuthority('USER', 'ADMIN', 'EMPLOYEE')")
    public ResponseEntity<EntityModel<DtoType>> createTicket(@AuthenticationPrincipal UserDetails userDetails,
                                                            @Validated @RequestBody TicketCreationDTO ticketCreationDTO){

        /*map the dto to entity*/
        Ticket raisedTicket = ticketMapper.dtoToEntity(ticketCreationDTO);
        /*set the necessary properties*/
        raisedTicket.setRaisedBy(userManagementService.getUserByUsername(userDetails.getUsername()));
        raisedTicket.setAssignedTo(userManagementService.getUserByUsername("kamar254baraka@outlook.com"));
        /*create the user*/
        Ticket savedTicket = ticketCreationService.createTicket(raisedTicket);
        /*construct a response*/
        DtoType info = new InfoDTO("ticket successfully created");
        EntityModel<DtoType> response = EntityModel.of(info);
        /*create links*/
        Link linkToTicket = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TicketManagementController.class).getTicketById(
                        savedTicket.getTicketId(), userDetails)).withRel("ticket");

        response.add(linkToTicket);

        /*return the response*/
        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }


}
