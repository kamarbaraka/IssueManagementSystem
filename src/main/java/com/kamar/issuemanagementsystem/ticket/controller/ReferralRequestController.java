package com.kamar.issuemanagementsystem.ticket.controller;

import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.ticket.entity.ReferralRequest;
import com.kamar.issuemanagementsystem.ticket.service.ReferralRequestManagementService;
import com.kamar.issuemanagementsystem.ticket.utility.mapper.ReferralRequestMapper;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * the referral request controller.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/tickets/referral"})
public class ReferralRequestController {

    private final ReferralRequestManagementService referralRequestManagementService;
    private final ReferralRequestMapper referralRequestMapper;

    /**
     * respond to referral request*/
    @GetMapping(value = {"{id}/{accept}"})
    @Operation(tags = {"Ticket Assignment"}, summary = "respond to a referral", description = "accept or reject ticket referral.",
    security = {@SecurityRequirement(name = "ADMIN"), @SecurityRequirement(name = "EMPLOYEE")})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<EntityModel<DtoType>> acceptReferralRequest(@PathVariable("accept") boolean accept,
                                                                      @PathVariable("id") long id){
        /*map to Referral request*/


        /*check weather accepted or rejected*/
        if (!accept){

            /*reject*/
            referralRequestManagementService.rejectReferralRequestById(id);
            /*construct info*/
            EntityModel<DtoType> response = EntityModel.of(new InfoDTO("rejected referral request"));
            /*return the response*/
            return ResponseEntity.ok(response);
        }

        /*accept*/
        referralRequestManagementService.acceptReferralRequestById(id);

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
     * get referral request by id.
     * @author kamar baraka.*/
    @GetMapping(value = {"{id}"})
    @Operation(tags = {"Ticket Assignment"},summary = "get referral",description = "get referral by id",
    security = {@SecurityRequirement(name = "ADMIN"), @SecurityRequirement(name = "EMPLOYEE")})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<EntityModel<DtoType>> getReferralRequestById(@PathVariable("id") long id){

        /*get referral*/
        ReferralRequest referral = referralRequestManagementService.getReferralRequestById(id);
        /*map to DTO*/
        DtoType referralDto = referralRequestMapper.entityToDTO(referral);
        /*construct a response*/
        EntityModel<DtoType> response = EntityModel.of(referralDto);
        /*return the response*/
        return ResponseEntity.ok(response);

    }
}
