package com.kamar.issuemanagementsystem.ticket.controller;

import com.kamar.issuemanagementsystem.ticket.data.TicketPriority;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketAssignmentDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketReferralDTO;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.service.TicketAssignmentService;
import com.kamar.issuemanagementsystem.ticket.service.TicketManagementService;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.FutureOrPresent;
import jdk.jfr.ContentType;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.naming.OperationNotSupportedException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * the ticket assignment controller.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/tickets/assign"})
@Log4j2
public class TicketAssignmentController {

    private final TicketAssignmentService ticketAssignmentService;
    private final TicketManagementService ticketManagementService;
    private final UserManagementService userManagementService;

    /**
     * assign a ticket to user
     */
    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Operation(tags = {"Ticket Assignment"}, summary = "assign a ticket. {'ADMIN', 'DEPARTMENT_ADMIN'}",
            description = "assign a ticket to a user.",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"ADMIN, DEPARTMENT_ADMIN"})})
    @RequestBody(content = {@Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE),
    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEPARTMENT_ADMIN')")
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> assignATicketTo(@RequestParam("ticket_number") String ticketNumber,
                                                                @Validated @RequestParam("to") @Email String username,
                                                                @Validated @RequestParam("priority") String priority,
                                                                @Validated @RequestParam("deadline") String deadline){

        if (priority == null) {
            priority = "medium";
        }
        /*create a dto*/
        TicketAssignmentDTO ticketAssignmentDTO = new TicketAssignmentDTO(ticketNumber, username,
                priority.toUpperCase(), deadline);

        /*assign ticket*/
        try {
            ticketAssignmentService.assignTo(ticketAssignmentDTO);
        } catch (OperationNotSupportedException e) {

            /*log and respond*/
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        /*construct a response*/
        DtoType infoDTO = new InfoDTO("ticket successfully assigned to " + username);

        EntityModel<DtoType> response = EntityModel.of(infoDTO);

        /*create links*/
        Link ticketLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TicketManagementController.class)
                        .getTicketById(ticketNumber)).withRel("ticket");

        response.add(ticketLink);

        /*return response*/
        return ResponseEntity.ok(response);
    }

}
