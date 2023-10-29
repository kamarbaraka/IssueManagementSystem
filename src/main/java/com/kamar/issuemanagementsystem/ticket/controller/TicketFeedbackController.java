package com.kamar.issuemanagementsystem.ticket.controller;

import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketUserFeedbackDTO;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketFeedbackException;
import com.kamar.issuemanagementsystem.ticket.service.TicketFeedbackService;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * the ticket feedback controller.
 * @author kamar baraka.*/

@RestController
@RequestMapping(value = {"api/v1/tickets/feedback"})
@RequiredArgsConstructor
public class TicketFeedbackController {

    private final TicketFeedbackService ticketFeedbackService;

    @PostMapping(value = {"{ticket_id}"})
    @Operation(tags = {"Ticket Feedback", "Ticket Submission"}, summary = "send a feedback on a ticket.")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER', 'EMPLOYEE')")
    public ResponseEntity<EntityModel<DtoType>> sendFeedback(@PathVariable("ticket_id") long ticketId,
                                                             @Validated @RequestBody TicketUserFeedbackDTO userFeedbackDTO,
                                                             @AuthenticationPrincipal UserDetails authenticatedUser){

        /*send the feedback*/
        try {
            ticketFeedbackService.sendFeedback(userFeedbackDTO, ticketId, authenticatedUser);
        } catch (TicketFeedbackException e) {

            /*compose and return response*/
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    EntityModel.of(
                            new InfoDTO(e.getMessage())
                    )
            );
        }

        /*compose the response*/
        return ResponseEntity.ok(
                EntityModel.of(
                        new InfoDTO("feedback successfully sent")
                )
        );
    }
}
