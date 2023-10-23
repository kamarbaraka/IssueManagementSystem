package com.kamar.issuemanagementsystem.ticket.controller;

import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.service.TicketManagementService;
import com.kamar.issuemanagementsystem.ticket.utility.mapper.TicketMapper;
import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * the ticket management controller.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/tickets/management"})
public class TicketManagementController {

    private final TicketManagementService ticketManagementService;
    private final TicketMapper ticketMapper;

    @GetMapping(value = {"{id}"})
    @Operation(tags = {"Ticket Creation", "Ticket Assignment", "Ticket Submission", "Ticket Management"},
            summary = "get a ticket", description = "get a ticket by ID")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE', 'USER')")
    public ResponseEntity<EntityModel<DtoType>> getTicketById(@PathVariable("id") long id,
                                                              @AuthenticationPrincipal UserDetails userDetails){

        /*get the ticket*/
        Ticket ticket = ticketManagementService.getTicketById(id);
        /*map the ticket*/
        DtoType adminDto = ticketMapper.entityToDTOAdmin(ticket);
        /*construct the response*/
        EntityModel<DtoType> response = EntityModel.of(adminDto);
        /*add links*/
        Link referLink = WebMvcLinkBuilder.linkTo(TicketAssignmentController.class).slash("refer")
                .slash(ticket.getTicketId()).withRel("refer");

        response.add(referLink);

        /*check authorities and perform actions based*/
        if (userDetails.getAuthorities().contains(Authority.USER) && !userDetails.getUsername().equals(ticket.getRaisedBy().getUsername())){
            /*return a response*/
            return ResponseEntity.ok(
                    EntityModel.of(new InfoDTO("operation not allowed"))
            );
        }

        if (userDetails.getAuthorities().contains(Authority.EMPLOYEE) &&
                (!userDetails.getUsername().equals(ticket.getRaisedBy().getUsername()) ||
                        (!userDetails.getUsername().equals(ticket.getAssignedTo().getUsername()))))
            return ResponseEntity.ok(
                        EntityModel.of(new InfoDTO("operation not allowed"))
            );

        /*return response*/
        return ResponseEntity.ok(response);
    }
}
