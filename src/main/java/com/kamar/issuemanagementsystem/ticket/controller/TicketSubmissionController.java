package com.kamar.issuemanagementsystem.ticket.controller;

import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketSubmissionException;
import com.kamar.issuemanagementsystem.ticket.service.TicketSubmissionService;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * the ticket submission controller.
 * @author kamar baraka.*/

@RestController
@RequestMapping(value = {"api/v1/tickets/submit"})
@RequiredArgsConstructor
@Log4j2
public class TicketSubmissionController {

    private final TicketSubmissionService ticketSubmissionService;


    @PostMapping(consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(tags = {"Ticket Submission"}, summary = "submit a ticket by id",
    security = {@SecurityRequirement(name = "basicAuth")})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    @RequestBody(content = {@Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)})
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> submitATicket(@RequestParam("ticket_id") long ticketId,
                                                              @AuthenticationPrincipal UserDetails authenticatedUser){

        /*submit the ticket*/
        try {
            ticketSubmissionService.submitTicket(ticketId, authenticatedUser);
        } catch (TicketSubmissionException e) {

            /*log and compose a response*/
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok(
                EntityModel.of(
                        new InfoDTO("submitted successfully")
                )
        );
    }
}
