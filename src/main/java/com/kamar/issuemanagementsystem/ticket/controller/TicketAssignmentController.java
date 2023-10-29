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
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
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
public class TicketAssignmentController {

    private final TicketAssignmentService ticketAssignmentService;
    private final TicketManagementService ticketManagementService;
    private final UserManagementService userManagementService;

    /**
     * assign a ticket to user
     */
    @PostMapping(value = {"{id}"})
    @Operation(tags = {"Ticket Assignment"}, summary = "assign a ticket", description = "assign a ticket to a user.")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EntityModel<DtoType>> assignATicketTo(@PathVariable("id") long id,
                                                                @Validated @RequestBody TicketAssignmentDTO ticketAssignmentDTO,
                                                                @AuthenticationPrincipal UserDetails userDetails){
        /*get the ticket*/
        Ticket ticket = ticketManagementService.getTicketById(id);
        /*get the user to assign to*/
        User userToAssign = userManagementService.getUserByUsername(ticketAssignmentDTO.assignTo());
        /*set the ticket*/
        /*deadline*/
        ticket.setAssignedTo(userToAssign);
        /*priority*/
        ticket.setPriority(TicketPriority.valueOf(ticketAssignmentDTO.priority()));
        /*deadline*/
        LocalDate deadline = LocalDate.parse(ticketAssignmentDTO.deadline(),
                DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        ticket.setDeadline(deadline);
        /*status*/
        ticket.setStatus(TicketStatus.ASSIGNED);

        /*assign ticket*/
        try {
            ticketAssignmentService.assignTo(ticket);
        } catch (OperationNotSupportedException e) {

            return ResponseEntity.ok(
                    EntityModel.of(
                            new InfoDTO("operation not supported")
                    )
            );
        }

        /*construct a response*/
        DtoType infoDTO = new InfoDTO("ticket successfully assigned to " + userToAssign.getUsername());

        EntityModel<DtoType> response = EntityModel.of(infoDTO);

        /*create links*/
        Link ticketLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(TicketManagementController.class)
                        .getTicketById(ticket.getTicketId(), userDetails)).withRel("ticket");

        response.add(ticketLink);

        /*return response*/
        return ResponseEntity.ok(response);
    }

    @PostMapping(value = {"refer/{ticketId}"})
    @Operation(tags = {"Ticket Assignment"}, summary = "refer a ticket", description = "refer a ticket to another user")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<EntityModel<DtoType>> referTicketToUser(@PathVariable("ticketId") long ticketId,
                                                                  @Validated @RequestBody TicketReferralDTO ticketReferralDTO){

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
