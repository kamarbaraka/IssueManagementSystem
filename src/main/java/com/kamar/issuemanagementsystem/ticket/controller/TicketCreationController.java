package com.kamar.issuemanagementsystem.ticket.controller;

import com.kamar.issuemanagementsystem.app_properties.InnitUserProperties;
import com.kamar.issuemanagementsystem.attachment.data.AttachmentDTO;
import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.attachment.utils.AttachmentMapperImpl;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketCreationDTO;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.service.TicketCreationService;
import com.kamar.issuemanagementsystem.ticket.service.TicketManagementService;
import com.kamar.issuemanagementsystem.ticket.utility.mapper.TicketMapper;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import com.kamar.issuemanagementsystem.user.service.UserManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartRequest;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import org.springframework.web.multipart.support.StandardMultipartHttpServletRequest;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * the controller for ticket creation.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"/api/v1/tickets"})
@Log4j2
public class TicketCreationController {

    private final TicketMapper ticketMapper;
    private final UserManagementService userManagementService;
    private final TicketCreationService ticketCreationService;
    private final InnitUserProperties innitUserProperties;

    /**
     * create a {@link }*/
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @Operation(tags = {"Ticket Creation"}, summary = "create a ticket. {'USER', 'EMPLOYEE'}",
            description = "use this api to raise a ticket",
    security = {@SecurityRequirement(name = "basicAuth")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
            @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE),
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
            @Content(mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE)
    })
    @PreAuthorize("hasAnyAuthority('USER', 'EMPLOYEE')")
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> createTicket(
                                                             @RequestParam("department") String departmentToAssign,
                                                             @RequestParam("title") String title,
                                                             @RequestParam("description") String description,
                                                             @RequestBody AttachmentDTO[] attachments){

        byte[] bytes = new byte[1024];


        /*get authenticated user*/
        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        List<AttachmentDTO> requestAttachments = attachments == null ? new ArrayList<>() : Arrays.asList(attachments);

        TicketCreationDTO ticketCreationDTO = new TicketCreationDTO(title, description, departmentToAssign, requestAttachments);
        Ticket savedTicket;

        try
        {
            /*map the dto to entity*/
            Ticket raisedTicket = ticketMapper.dtoToEntity(ticketCreationDTO);

            /*set the necessary properties*/
            raisedTicket.setRaisedBy(userManagementService.getUserByUsername(userDetails.getUsername()));
            raisedTicket.setAssignedTo(userManagementService.getUserByUsername(innitUserProperties.username()));
            /*create the ticket*/
            savedTicket = ticketCreationService.createTicket(raisedTicket);
        }catch (Exception e){

            /*log the exception*/
            log.error(e.getMessage());
            /*respond*/
            return ResponseEntity.badRequest().build();
        }


        /*construct a response*/
        DtoType info = new InfoDTO(savedTicket.getTicketNumber());
        EntityModel<DtoType> response = EntityModel.of(info);

        /*return the response*/
        return ResponseEntity.ok(response);

    }


}
