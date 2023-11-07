package com.kamar.issuemanagementsystem.reporting.controller;

import com.kamar.issuemanagementsystem.ticket.controller.TicketManagementController;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.reporting.service.TicketReportingService;
import com.kamar.issuemanagementsystem.ticket.utility.mapper.TicketMapper;
import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
 * ticket reporting controller.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/tickets/report"})
@Log4j2
public class TicketReportingController {

    private final TicketReportingService ticketReportingService;
    private final TicketMapper ticketMapper;
    private final UserManagementService userManagementService;


    private List<EntityModel<DtoType>> convertToDto(List<Ticket> ticketList, UserDetails userDetails){

        /*map to dto*/
        return ticketList.stream().map(ticket -> {
            /*map the ticket to dto*/
            DtoType ticketDto = ticketMapper.entityToDTOAdmin(ticket);
            /*convert to entity model*/
            EntityModel<DtoType> response = EntityModel.of(ticketDto);
            /*add Links*/
            Link ticketLink = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(TicketManagementController.class)
                            .getTicketById(ticket.getTicketId(), userDetails)
            ).withRel("ticket");

            response.add(ticketLink);
            return response;
        }).toList();
    }

    @GetMapping(value = {"status/{status}"})
    @Operation(tags = {"Ticket Reporting", "Ticket Analysis"}, summary = "Get tickets by the provided status",
    security = {@SecurityRequirement(name = "basicAuth")})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE', 'OWNER')")
    public ResponseEntity<List<EntityModel<DtoType>>> getTicketsByStatus(@PathVariable("status") String status,
                                                                         @AuthenticationPrincipal UserDetails userDetails){

        List<Ticket> tickets;

        try
        {
            /*get tickets by status*/
            tickets = ticketReportingService.ticketsByStatus(TicketStatus.valueOf(status), userDetails);
        }catch (Exception e){

            /*log and respond*/
            log.error( e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        /*map the tickets to DTO*/
        List<EntityModel<DtoType>> response = convertToDto(tickets, userDetails);

        /*construct a response*/
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(tags = {"Ticket Reporting", "Ticket Analysis"}, summary = "get all the tickets in the database",
    security = {@SecurityRequirement(name = "basicAuth")})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE', 'USER', 'OWNER')")
    public ResponseEntity<List<EntityModel<DtoType>>> getAllTickets(@AuthenticationPrincipal UserDetails userDetails){


        List<Ticket> allTickets;

        try
        {
            /*get all tickets*/
            allTickets = ticketReportingService.getAllTickets(userDetails);
        }catch (Exception e){

            /*log and respond*/
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        /*map to dto*/
        List<EntityModel<DtoType>> response = convertToDto(allTickets, userDetails);
        /*return the response*/
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = {"users/{username}/status/{status}"})
    @Operation(tags = {"Ticket Reporting"}, summary = "get tickets by the user and status",
    security = {@SecurityRequirement(name = "basicAuth")})
    @PreAuthorize("hasAnyAuthority('ADMIN','EMPLOYEE')")
    public ResponseEntity<List<EntityModel<DtoType>>> getTicketsByUserAndStatus(@PathVariable("username") String username,
                                                                                @PathVariable("status") String status,
                                                                                @AuthenticationPrincipal
                                                                                    UserDetails userDetails){

        User currentUser;
        List<Ticket> tickets;

        try
        {
            /*get user*/
            currentUser = userManagementService.getUserByUsername(userDetails.getUsername());
            /*get the tickets*/
            tickets = ticketReportingService.userTicketsByStatus(currentUser, TicketStatus.valueOf(status));
        }catch (Exception e){

            /*log and respond*/
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        /*check authority*/
        if (userDetails.getAuthorities().contains(Authority.EMPLOYEE)){

            /*map to dto*/
            List<EntityModel<DtoType>> response = convertToDto(tickets, userDetails);
            /*return the response*/
            return ResponseEntity.ok(response);
        }


        /*map the tickets*/
        List<EntityModel<DtoType>> response = convertToDto(tickets, userDetails);
        /*return the response*/
        return ResponseEntity.ok(response);

    }
}
