package com.kamar.issuemanagementsystem.ticket.controller;

import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.MembersDto;
import com.kamar.issuemanagementsystem.ticket.data.dto.ReferralRequestDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketReferralDTO;
import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.exceptions.ReferralRequestException;
import com.kamar.issuemanagementsystem.ticket.service.ReferralRequestManagementService;
import com.kamar.issuemanagementsystem.ticket.service.ReferralRequestReportService;
import com.kamar.issuemanagementsystem.ticket.service.TicketManagementService;
import com.kamar.issuemanagementsystem.ticket.utility.mapper.ReferralRequestMapper;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * the referral request controller.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/tickets/referral"})
@Log4j2
public class ReferralRequestController {

    private final ReferralRequestManagementService referralRequestManagementService;
    private final ReferralRequestMapper referralRequestMapper;
    private final TicketManagementService ticketManagementService;
    private final ReferralRequestReportService requestReportService;

    /**
     * respond to referral request*/
    @GetMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(tags = { "Ticket Referral"}, summary = "respond to a referral. {'EMPLOYEE'}",
            description = "accept or reject ticket referral.",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"EMPLOYEE"})})
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @RequestBody(content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
            @Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)})
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> respondToReferralRequest(@RequestParam("accept") boolean accept,
                                                                         @RequestParam("referral_id") long id){

        /*check weather accepted or rejected*/
        try {
            referralRequestManagementService.respondToReferralRequest(id, accept);
        } catch (Exception e) {

            /*log and respond*/
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        if (!accept) {
            /*construct info*/
            EntityModel<DtoType> response = EntityModel.of(new InfoDTO("rejected referral request"));
            /*return the response*/
            return ResponseEntity.ok(response);
        }


        /*construct response*/
        EntityModel<DtoType> response = EntityModel.of(new InfoDTO("request referral accepted"));
        /*add link*/
        Link referralLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                ReferralRequestController.class).getReferralRequestById(id)).withRel("referral");

        response.add(referralLink);
        /*return response*/
        return ResponseEntity.ok(response);
    }

    /**
     * get referral request by ticketNumber.
     * @author kamar baraka.*/
    @GetMapping(value = {"byId"}, consumes = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(tags = {"Ticket Referral"},summary = "get referral. {'EMPLOYEE'}",description = "get referral by ticketNumber",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"EMPLOYEE"})})
    @PreAuthorize("hasAuthority('EMPLOYEE')")
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> getReferralRequestById(@RequestParam("referral_id") long id){

        ReferralRequest referral;

        try
        {
            /*get referral*/
            referral = referralRequestManagementService.getReferralRequestById(id);
        }catch (Exception e){

            /*log and respond*/
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        /*map to DTO*/
        DtoType referralDto = referralRequestMapper.entityToDTO(referral);
        /*construct a response*/
        EntityModel<DtoType> response = EntityModel.of(referralDto);
        /*return the response*/
        return ResponseEntity.ok(response);

    }

    @PostMapping(value = {"refer"}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(tags = { "Ticket Referral"}, summary = "refer a ticket. {\"ADMIN\", \"EMPLOYEE\", \"DEPARTMENT_ADMIN\"}",
            description = "refer a ticket to another user",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"ADMIN", "EMPLOYEE", "DEPARTMENT_ADMIN"})})
    @RequestBody(content = {
            @Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE),
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    })
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEPARTMENT_ADMIN', 'EMPLOYEE')")
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> referTicketToUser(@RequestParam("ticket_number") String ticketNumber,
                                                                  @Validated @RequestParam("to") @Email String username){

        /*create a dto*/
        TicketReferralDTO ticketReferralDTO = new TicketReferralDTO(username);

        Ticket ticket;
        try
        {
            /*get the ticket*/
            ticket = ticketManagementService.getTicketById(ticketNumber);
            /*refer*/
            ReferralRequestDTO referralRequestDTO = referralRequestManagementService.referTicketTo(ticket, ticketReferralDTO.To());

            /*construct a response*/
            EntityModel<DtoType> response = EntityModel.of(referralRequestDTO);

            /*return response*/
            return ResponseEntity.ok(response);

        }catch (Exception e){

            /*log and respond*/
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

    }


    @GetMapping(value = {"refer"})
    @Operation(tags = {"Ticket Referral"}, summary = "Api to get users to refer ticket to. {EMPLOYEE, ADMIN, DEPARTMENT_ADMIN" +
            "}", security = {
            @SecurityRequirement(name = "basicAuth")
    })
    @PreAuthorize("hasAnyAuthority('EMPLOYEE', 'DEPARTMENT_ADMIN')")
    @CrossOrigin
    public ResponseEntity<MembersDto> refer(){

        /*get members*/
        try {
            MembersDto membersDto = referralRequestManagementService.refer();
            return ResponseEntity.ok(membersDto);
        } catch (ReferralRequestException e) {
            /*log*/
            log.warn(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping(value = {"to"})
    @Operation(
            tags = {"Ticket Referral"},
            summary = "Api to get referrals to a user. {'ADMIN', 'EMPLOYEE'}",
            security = {@SecurityRequirement(name = "basicAuth")}
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    @CrossOrigin
    public ResponseEntity<List<ReferralRequestDTO>> getReferralRequestsTo(@RequestParam("to") @Validated @Email String to){

        /*get the referral requests*/
        List<ReferralRequest> referralRequests = requestReportService.getReferralRequestsTo(to);

        /*map to dto*/
        List<ReferralRequestDTO> requestDTOS = referralRequests.stream().map(referralRequestMapper::entityToDTO).toList();

        /*construct a response*/
        return ResponseEntity.ok(requestDTOS);
    }

    @GetMapping(value = {"from"})
    @Operation(
            tags = {"Ticket Referral"},
            summary = "Api to get referrals from user. {'ADMIN', 'EMPLOYEE'}",
            security = {@SecurityRequirement(name = "basicAuth")}
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    @CrossOrigin
    public ResponseEntity<List<ReferralRequestDTO>> getReferralsFrom(@RequestParam("from") @Validated @Email String from){

        /*get the referrals*/
        List<ReferralRequest> referralRequests = requestReportService.getReferralRequestsFrom(from);

        /*map to dto*/
        List<ReferralRequestDTO> requestDTOS = referralRequests.stream().map(referralRequestMapper::entityToDTO).toList();

        /*construct a response*/
        return ResponseEntity.ok(requestDTOS);
    }

    @GetMapping(value = {"accepted"})
    @Operation(
            tags = {"Ticket Referral"},
            summary = "Api to get accepted requests from user. {'ADMIN', 'EMPLOYEE'}",
            security = {@SecurityRequirement(name = "basicAuth")}
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    @CrossOrigin
    public ResponseEntity<List<ReferralRequestDTO>> getAcceptedRequestsFrom(@RequestParam("from") @Validated @Email
                                                                         String from){

        /*get the accepted referrals*/
        List<ReferralRequest> acceptedRequests = requestReportService.getAcceptedRequestsFrom(from);

        /*map to dto*/
        List<ReferralRequestDTO> requestDTOS = acceptedRequests.stream().map(referralRequestMapper::entityToDTO).toList();

        /*construct a response*/
        return ResponseEntity.ok(requestDTOS);
    }

    @GetMapping(value = {"rejected"})
    @Operation(
            tags = {"Ticket Referral"},
            summary = "Api to get rejected referrals from user. {'ADMIN', 'EMPLOYEE'}",
            security = {@SecurityRequirement(name = "basicAuth")}
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    @CrossOrigin
    public ResponseEntity<List<ReferralRequestDTO>> getRejectedRequestsFrom(@RequestParam("from") @Validated @Email
                                                                         String from){

        /*get the rejected requests*/
        List<ReferralRequest> rejectedRequests = requestReportService.getRejectedRequestsFrom(from);

        /*map to dto*/
        List<ReferralRequestDTO> requestDTOS = rejectedRequests.stream().map(referralRequestMapper::entityToDTO).toList();

        /*construct a response*/
        return ResponseEntity.ok(requestDTOS);
    }
}
