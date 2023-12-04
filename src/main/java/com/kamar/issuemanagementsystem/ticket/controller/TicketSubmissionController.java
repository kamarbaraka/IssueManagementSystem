package com.kamar.issuemanagementsystem.ticket.controller;

import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketException;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketSubmissionException;
import com.kamar.issuemanagementsystem.ticket.service.TicketSubmissionService;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nonnull;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
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


    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(tags = {"Ticket Submission"}, summary = "submit a ticket by ticketNumber. {'EMPLOYEE'}",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"EMPLOYEE"})})
    @RequestBody(content = {@Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE),
    @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @PreAuthorize("hasAnyAuthority('EMPLOYEE')")
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> submitATicket(@Validated @RequestParam("ticket_number") @Nonnull String ticketNumber,
                                                              @Validated @RequestParam("solution") @Nonnull String solution){

        /*submit the ticket*/
        try {
            ticketSubmissionService.submitTicket(ticketNumber, solution);
        } catch (TicketSubmissionException | TicketException e) {

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
