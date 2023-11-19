package com.kamar.issuemanagementsystem.ticket.controller;

import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketAdminPresentationDTO;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketException;
import com.kamar.issuemanagementsystem.ticket.service.TicketManagementService;
import com.kamar.issuemanagementsystem.ticket.utility.mapper.TicketMapper;
import com.kamar.issuemanagementsystem.ticket.utility.util.TicketUtilities;
import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

/**
 * the ticket management controller.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/tickets/management"})
@Log4j2
public class TicketManagementController {

    private final TicketManagementService ticketManagementService;
    private final TicketMapper ticketMapper;
    private final TicketUtilities ticketUtilities;

    @GetMapping(value = {"get"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(tags = {"Ticket Creation", "Ticket Assignment", "Ticket Submission", "Ticket Management"},
            summary = "get a ticket", description = "get a ticket by ID",
    security = {@SecurityRequirement(name = "basicAuth")})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE', 'USER')")
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> getTicketById(@RequestParam("ticket_id") long id,
                                                              @AuthenticationPrincipal UserDetails userDetails){

        Ticket ticket;
        try
        {
            /*get the ticket*/
            ticket = ticketManagementService.getTicketById(id);
        }catch (Exception e){

            /*log and respond*/
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        /*map the ticket*/
        TicketAdminPresentationDTO adminDto = ticketMapper.entityToDTOAdmin(ticket);
        /*construct the response*/
        EntityModel<DtoType> response = EntityModel.of(adminDto);
        /*add links*/
        Link attachmentLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(TicketManagementController.class)
                .downloadTicketAttachments(adminDto.id())).withRel("attachment");

        Link referLink = WebMvcLinkBuilder.linkTo(TicketAssignmentController.class).slash("refer")
                .slash(ticket.getTicketId()).withRel("refer");

        response.add(referLink);
        response.add(attachmentLink);

        /*check authorities and perform actions based*/
        if (userDetails.getAuthorities().contains(Authority.USER) && !userDetails.getUsername().equals(ticket.getRaisedBy().getUsername())){
            /*return a response*/
            return ResponseEntity.badRequest().body(
                    EntityModel.of(new InfoDTO("operation not allowed"))
            );
        }


        if (userDetails.getAuthorities().contains(Authority.EMPLOYEE) &&
                (!userDetails.getUsername().equals(ticket.getAssignedTo().getUsername()) &&
                    (!userDetails.getUsername().equals(ticket.getRaisedBy().getUsername()))))
                    {
                        return ResponseEntity.badRequest().body(
                            EntityModel.of(new InfoDTO("you are not permitted to access this resource"))
                    );

        }

        /*return response*/
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = {"attachment"}, produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @Operation(tags = {"Ticket Management"}, summary = "Api to download ticket attachment by ticket id",
    security = {@SecurityRequirement(name = "basicAuth")})
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    public ResponseEntity<byte[]> downloadTicketAttachments(@RequestParam("ticket_id") long id){

        Attachment attachment;
        try {
            /*get the attachment*/
            attachment = ticketManagementService.downloadTicketAttachment(id);
        } catch (TicketException e) {

            /*log*/
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        if (attachment == null) {
            return ResponseEntity.noContent().build();
        }

        /*get the bytes*/
        byte[] content = ticketUtilities.convertBlobToBytes(attachment.getContent());

        /*construct the headers*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentLength(content.length);
        headers.setContentDisposition(ContentDisposition.attachment().filename(attachment.getFilename()).build());

        /*compose and return the response*/
        return ResponseEntity.ok().headers(headers).body(content);

    }
}
