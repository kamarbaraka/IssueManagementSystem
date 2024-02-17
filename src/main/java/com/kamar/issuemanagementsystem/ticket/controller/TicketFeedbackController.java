package com.kamar.issuemanagementsystem.ticket.controller;

import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketUserFeedbackDTO;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketException;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketFeedbackException;
import com.kamar.issuemanagementsystem.ticket.service.TicketFeedbackService;
import com.kamar.issuemanagementsystem.user_management.data.dto.DtoType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

/**
 * the ticket feedback controller.
 * @author kamar baraka.*/

@RestController
@RequestMapping(value = {"api/v1/tickets/feedback"})
@RequiredArgsConstructor
@Log4j2
public class TicketFeedbackController {

    private final TicketFeedbackService ticketFeedbackService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(tags = {"Ticket Feedback", "Ticket Submission"}, summary = "send a feedback on a ticket. {'USER', 'EMPLOYEE'}",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"USER"})})
    @PreAuthorize("hasAnyAuthority('USER', 'EMPLOYEE')")
    @RequestBody(content = {
            @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE),
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    })
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> sendFeedback(@RequestParam("ticket_number") String ticketNumber,
                                                             @RequestParam(value = "feedback", required = false)
                                                             String feedback,
                                                             @Validated @RequestParam("satisfied") boolean satisfied,
                                                             @Validated @RequestParam(value = "rating")
                                                                 @Max(value = 5, message = "max rating is 5")
                                                             @Min(value = 0) int rating,
                                                             @Validated
                                                             @org.springframework.web.bind.annotation.RequestBody(required = false)
                                                                     @Nullable
                                                             List<MultipartFile> attachments){

        /*check if attachments is null*/
        if (attachments == null) {
            attachments = new ArrayList<>();
        }

        /*create a dto*/
        TicketUserFeedbackDTO userFeedbackDTO = new TicketUserFeedbackDTO(
                ticketNumber,
                feedback,
                satisfied,
                rating,
                attachments
        );

        /*send the feedback*/
        try {
            ticketFeedbackService.sendFeedback(userFeedbackDTO);
        } catch (TicketFeedbackException | TicketException e) {

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
