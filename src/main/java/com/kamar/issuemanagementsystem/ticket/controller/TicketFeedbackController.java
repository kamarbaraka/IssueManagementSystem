package com.kamar.issuemanagementsystem.ticket.controller;

import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketUserFeedbackDTO;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketFeedbackException;
import com.kamar.issuemanagementsystem.ticket.service.TicketFeedbackService;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * the ticket feedback controller.
 * @author kamar baraka.*/

@RestController
@RequestMapping(value = {"api/v1/tickets/feedback"})
@RequiredArgsConstructor
@Log4j2
public class TicketFeedbackController {

    private final TicketFeedbackService ticketFeedbackService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(tags = {"Ticket Feedback", "Ticket Submission"}, summary = "send a feedback on a ticket.",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"USER"})})
    @PreAuthorize("hasAnyAuthority('USER', 'EMPLOYEE')")
    @RequestBody(content = {
            @Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE),
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    })
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> sendFeedback(@RequestParam("ticket_id") long ticketId,
                                                             @Validated @RequestParam("feedback") String feedback,
                                                             @Validated @RequestParam("satisfied") boolean satisfied,
                                                             @Validated @RequestParam("rating") @Max(value = 5, message = "max rating is 5")
                                                             @Min(value = 0) int rating,
                                                             @AuthenticationPrincipal UserDetails authenticatedUser){

        /*create a dto*/
        TicketUserFeedbackDTO userFeedbackDTO = new TicketUserFeedbackDTO(feedback, satisfied, rating);

        /*send the feedback*/
        try {
            ticketFeedbackService.sendFeedback(userFeedbackDTO, ticketId, authenticatedUser);
        } catch (TicketFeedbackException e) {

            /*log and return response*/
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        /*compose the response*/
        return ResponseEntity.ok(
                EntityModel.of(
                        new InfoDTO("feedback successfully sent")
                )
        );
    }
}
