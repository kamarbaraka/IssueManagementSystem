package com.kamar.issuemanagementsystem.ticket.controller;

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
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * the ticket assignment controller.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/tickets/assign"})
public class TicketAssignmentController {

    private final TicketAssignmentService ticketAssignmentService;
    private final TicketManagementService ticketManagementService;
    private final UserManagementService userManagementService;

    /**
     * assign a ticket to user
     */
    @GetMapping(value = {"{id}"})
    @Operation(tags = {"Ticket Assignment"}, summary = "assign a ticket", description = "assign a ticket to a user.")
    public ResponseEntity<EntityModel<DtoType>> assignATicketTo(@PathVariable("id") long id,
                                                                @RequestBody TicketAssignmentDTO ticketAssignmentDTO){
        /*get the ticket*/
        Ticket ticket = ticketManagementService.getTicketById(id);
        /*get the user to assign to*/
        User userToAssign = userManagementService.getUserByUsername(ticketAssignmentDTO.assignTo());
        /*set the ticket*/
        ticket.setAssignedTo(userToAssign);

        LocalDate deadline = LocalDate.parse(ticketAssignmentDTO.deadline(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        ticket.setDeadline(deadline);
        /*assign ticket*/
        ticketAssignmentService.assignTo(ticket);

        /*construct a response*/
        DtoType infoDTO = new InfoDTO("ticket successfully assigned to " + userToAssign.getUsername());

        EntityModel<DtoType> response = EntityModel.of(infoDTO);

        /*create links*/
        Link ticketLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TicketCreationController.class)
                        .getTicketById(ticket.getTicketId())).withRel("ticket");

        response.add(ticketLink);

        /*return response*/
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = {"refer/{ticketId}"})
    @Operation(tags = {"Ticket Assignment"}, summary = "refer a ticket", description = "refer a ticket to another user")
    public ResponseEntity<EntityModel<DtoType>> referTicketToUser(@PathVariable("ticketId") long ticketId,
                                                                  @RequestBody TicketReferralDTO ticketReferralDTO){

        /*get the ticket*/
        Ticket ticket = ticketManagementService.getTicketById(ticketId);
        /*refer*/
        ticketAssignmentService.referTicketTo(ticket, ticketReferralDTO.To());

        /*construct a response*/
        DtoType infoDTO = new InfoDTO("referral request successfully sent");
        EntityModel<DtoType> response = EntityModel.of(infoDTO);

        /*return response*/
        return ResponseEntity.ok(response);

    }
}
