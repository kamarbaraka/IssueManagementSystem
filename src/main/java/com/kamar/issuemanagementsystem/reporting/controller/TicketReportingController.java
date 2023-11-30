package com.kamar.issuemanagementsystem.reporting.controller;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.ticket.controller.TicketManagementController;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.reporting.service.TicketReportingService;
import com.kamar.issuemanagementsystem.ticket.utility.mapper.TicketMapper;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

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
    private final UserAuthorityUtility userAuthorityUtility;


    private List<EntityModel<DtoType>> convertToDto(List<Ticket> ticketList){

        /*map to dto*/
        return ticketList.stream().map(ticket -> {
            /*map the ticket to dto*/
            DtoType ticketDto = ticketMapper.entityToDTOAdmin(ticket);
            /*convert to entity model*/
            EntityModel<DtoType> response = EntityModel.of(ticketDto);
            /*add Links*/
            Link ticketLink = WebMvcLinkBuilder.linkTo(
                    WebMvcLinkBuilder.methodOn(TicketManagementController.class)
                            .getTicketById(ticket.getTicketNumber())
            ).withRel("ticket");

            response.add(ticketLink);
            return response;
        }).toList();
    }

    @GetMapping(value = {"status"}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(tags = {"Ticket Reporting", "Ticket Analysis"}, summary = "Get tickets by the provided status",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"AUTHENTICATED"})})
    @RequestBody(content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
            @Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    })
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    public ResponseEntity<List<EntityModel<DtoType>>> getTicketsByStatus(@RequestParam("status") String status){

        List<Ticket> tickets;

        try
        {
            /*get tickets by status*/
            tickets = ticketReportingService.ticketsByStatus(TicketStatus.valueOf(status.toUpperCase()));
        }catch (Exception e){

            /*log and respond*/
            log.error( e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        /*map the tickets to DTO*/
        List<EntityModel<DtoType>> response = convertToDto(tickets);

        /*construct a response*/
        return ResponseEntity.ok(response);
    }

    @GetMapping
    @Operation(tags = {"Ticket Reporting", "Ticket Analysis"}, summary = "get all the tickets in the database",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"AUTHENTICATED"})})
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    public ResponseEntity<List<EntityModel<DtoType>>> getAllTickets(){


        List<Ticket> allTickets;

        try
        {
            /*get all tickets*/
            allTickets = ticketReportingService.getAllTickets();
        }catch (Exception e){

            /*log and respond*/
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        /*map to dto*/
        List<EntityModel<DtoType>> response = convertToDto(allTickets);
        /*return the response*/
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = {"users/status"},consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE,MediaType.APPLICATION_JSON_VALUE})
    @Operation(tags = {"Ticket Reporting"}, summary = "get tickets by the user and status",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"AUTHENTICATED"})})
    @RequestBody(content = {@Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE),
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    public ResponseEntity<List<EntityModel<DtoType>>> getTicketsByUserAndStatus(@RequestParam("username") String username,
                                                                                @RequestParam("status") String status){

        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        User currentUser;
        List<Ticket> tickets;

        try
        {
            /*get user*/
            currentUser = userManagementService.getUserByUsername(userDetails.getUsername());
            /*get the tickets*/
            if (userDetails.getAuthorities().contains(userAuthorityUtility.getFor("admin"))) {
                User user = userManagementService.getUserByUsername(username);
                tickets = ticketReportingService.userTicketsByStatus(user, TicketStatus.valueOf(status.toUpperCase()));
            }
            else
                tickets = ticketReportingService.userTicketsByStatus(currentUser, TicketStatus.valueOf(status.toUpperCase()));
        }catch (Exception e){

            /*log and respond*/
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }


        /*map the tickets*/
        List<EntityModel<DtoType>> response = convertToDto(tickets);
        /*return the response*/
        return ResponseEntity.ok(response);

    }
}
