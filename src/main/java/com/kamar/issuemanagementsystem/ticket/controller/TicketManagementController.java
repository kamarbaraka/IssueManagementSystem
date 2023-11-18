package com.kamar.issuemanagementsystem.ticket.controller;

import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketAdminPresentationDTO;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.service.TicketManagementService;
import com.kamar.issuemanagementsystem.ticket.utility.mapper.TicketMapper;
import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.FileOutputStream;
import java.util.Collection;
import java.util.List;

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

    @GetMapping( produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(tags = {"Ticket Creation", "Ticket Assignment", "Ticket Submission", "Ticket Management"},
            summary = "get a ticket", description = "get a ticket by ID",
    security = {@SecurityRequirement(name = "basicAuth")})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE', 'USER')")
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> getTicketById(@RequestParam("id") long id,
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
        Link referLink = WebMvcLinkBuilder.linkTo(TicketAssignmentController.class).slash("refer")
                .slash(ticket.getTicketId()).withRel("refer");

        response.add(referLink);

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

        /*set the headers*/
        /*HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setContentLength(adminDto.attachments().stream().map(attachment -> attachment.length)
                .reduce(Integer::sum).orElseThrow());
        httpHeaders.setContentDispositionFormData("attachment", "attachment.pdf");*/

        /*return response*/
        return ResponseEntity.ok().body(response);
    }
}
