package com.kamar.issuemanagementsystem.ticket.controller;

import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketSubmissionException;
import com.kamar.issuemanagementsystem.ticket.service.TicketSubmissionService;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * the ticket submission controller.
 * @author kamar baraka.*/

@RestController
@RequestMapping(value = {"api/v1/tickets/submit"})
@RequiredArgsConstructor
public class TicketSubmissionController {

    private final TicketSubmissionService ticketSubmissionService;


    @PostMapping(value = {"{ticket_id}"})
    @Operation(tags = {"Ticket Submission"}, summary = "submit a ticket by id")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<EntityModel<DtoType>> submitATicket(@PathVariable("ticket_id") long ticketId,
                                                              @AuthenticationPrincipal UserDetails authenticatedUser){

        /*submit the ticket*/
        try {
            ticketSubmissionService.submitTicket(ticketId, authenticatedUser);
        } catch (TicketSubmissionException e) {

            /*compose a response*/
            return ResponseEntity.status(HttpStatus.CONFLICT).body (
                    EntityModel.of(
                            new InfoDTO(e.getMessage()),
                            WebMvcLinkBuilder.linkTo(TicketSubmissionController.class).withSelfRel()
                    ));
        }

        return ResponseEntity.ok(
                EntityModel.of(
                        new InfoDTO("submitted successfully")
                )
        );
    }
}
