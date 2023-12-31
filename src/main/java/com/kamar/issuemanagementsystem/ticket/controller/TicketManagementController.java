package com.kamar.issuemanagementsystem.ticket.controller;

import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.attachment.utils.AttachmentUtilityService;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.data.dto.TicketAdminPresentationDTO;
import com.kamar.issuemanagementsystem.ticket.entity.Ticket;
import com.kamar.issuemanagementsystem.ticket.exceptions.TicketException;
import com.kamar.issuemanagementsystem.ticket.service.TicketManagementService;
import com.kamar.issuemanagementsystem.ticket.utility.mapper.TicketMapper;
import com.kamar.issuemanagementsystem.ticket.utility.util.TicketUtilities;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.utility.util.UserUtilityService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.io.FileSystemResource;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.IOException;
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
    private final DepartmentRepository departmentRepository;
    private final AttachmentUtilityService attachmentUtilityService;
    private final UserUtilityService userUtilityService;

    @GetMapping(value = {"get"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(tags = {"Ticket Creation", "Ticket Assignment", "Ticket Submission", "Ticket Management"},
            summary = "get a ticket", description = "get a ticket by ID",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"AUTHENTICATED"})})
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> getTicketById(@RequestParam("ticket_number") String ticketNumber){

        Ticket ticket;
        try
        {
            /*get the ticket*/
            ticket = ticketManagementService.getTicketById(ticketNumber);
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
                .downloadTicketAttachments(adminDto.ticketNumber())).withRel("attachments");

        Link referLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                ReferralRequestController.class).refer()).withRel("refer");

        response.add(referLink);
        response.add(attachmentLink);

        /*return response*/
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = {"attachment"}, produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @Operation(tags = {"Ticket Management"}, summary = "Api to download ticket attachments by ticket ticketNumber",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"AUTHENTICATED"})})
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    public ResponseEntity<FileSystemResource> downloadTicketAttachments(@RequestParam("ticket_number") String ticketNumber){

        List<Attachment> attachments;
        try {
            /*get the attachments*/
            attachments = ticketManagementService.downloadTicketAttachment(ticketNumber);
        } catch (TicketException e) {

            /*log*/
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        if (attachments.isEmpty()) {
            return ResponseEntity.noContent().build();
        }

        if (attachments.size() == 1) {

            Attachment attachment = attachments.get(0);

            /*convert the attachments to file*/
            File attachmentFile = null;
            try {
                attachmentFile = attachmentUtilityService.convertAttachmentToFile(attachment);
            } catch (IOException e) {
                /*log*/
                log.error(e.getMessage());
                return ResponseEntity.internalServerError().build();
            }
            FileSystemResource attachmentResource = new FileSystemResource(attachmentFile);

            /*construct the headers*/
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            headers.setContentLength(attachmentFile.length());
            headers.setContentDisposition(ContentDisposition.attachment().filename(attachment.getFilename()).build());

            /*compose and return the response*/
            return ResponseEntity.ok().headers(headers).body(attachmentResource);
        }

        /*get files from the attachments*/
        List<File> attachmentFiles = attachments.stream().map(attachment -> {
            try {
                return attachmentUtilityService.convertAttachmentToFile(attachment);
            } catch (IOException e) {
                /*log*/
                log.error(e.getMessage());
                return null;
            }
        }).toList();

        /*compress the files to zip*/
        File zipFile;
        try {
            zipFile = attachmentUtilityService.compressFilesToZip(attachmentFiles, "attachments");
        } catch (Exception e) {
            /*log*/
            log.error(e.getMessage());
            return ResponseEntity.internalServerError().build();
        }

        /*create a resource from the zip*/
        FileSystemResource attachmentResource = new FileSystemResource(zipFile);

        /*construct the headers*/
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        try {
            headers.setContentLength(attachmentResource.contentLength());
        } catch (IOException e) {
            /*log*/
            log.error(e.getMessage());
        }
        headers.setContentDisposition(ContentDisposition.attachment().filename("attachments.zip").build());

        return ResponseEntity.ok().headers(headers).body(attachmentResource);
    }

    @GetMapping(value = {"getByDeptAndStatus"}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(tags = {"Ticket Management", "Department Management"}, summary = "Api to get tickets by status that belong to a department. {'ADMIN', 'DEPARTMENT_ADMIN'}",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"ADMIN", "DEPARTMENT_ADMIN"})})
    @RequestBody(content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
            @Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    })
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEPARTMENT_ADMIN')")
    @CrossOrigin
    public ResponseEntity<List<? extends DtoType>> getTicketsByDepartmentAndStatus(
                                                                   @RequestParam("department") String department,
                                                                   @RequestParam("status") String status){
        /*get the authenticated user*/
        UserDetails authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        try
        {
            /*get the authorities and status*/
            TicketStatus ticketStatus = TicketStatus.valueOf(status.toUpperCase());
            /*check the authority if admin or department admin*/
            if (userUtilityService.hasAuthority(authenticatedUser, "admin")) {

                /*get parsed department*/
                Department passedDept = departmentRepository.findDepartmentByDepartmentName(department).orElseThrow();
                /*get the tickets by status*/
                List<TicketAdminPresentationDTO> ticketsByStatus = ticketManagementService.getTicketsByDepartmentAndStatus(
                        passedDept, ticketStatus);
                /*compose the response*/
                return ResponseEntity.ok().body(ticketsByStatus);
            }

            /*get the department*/
            User user = (User) authenticatedUser;
            Department dept = departmentRepository.findDepartmentByMembersContaining(user).orElseThrow();
            /*get the tickets*/
            List<TicketAdminPresentationDTO> ticketsByStatus = ticketManagementService.getTicketsByDepartmentAndStatus(
                    dept, ticketStatus);
            /*respond*/
            return ResponseEntity.ok(ticketsByStatus);
        }catch (Exception e){
            /*log and respond*/
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping(value = {"getByDept"}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(tags = {"Ticket Management", "Department Management"}, summary = "Api to get tickets belonging to a department.",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"ADMIN", "DEPARTMENT_ADMIN"})})
    @RequestBody(content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
            @Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    })
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEPARTMENT_ADMIN')")
    @CrossOrigin
    public ResponseEntity<List<? extends DtoType>> getTicketsByDepartment(
            @RequestParam("department") String department
    ){
        /*get authenticated user*/
        UserDetails authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        try
        {
            /*check the authority if admin or department admin*/
            if (userUtilityService.hasAuthority(authenticatedUser, "admin")) {

                /*get parsed department*/
                Department passedDept = departmentRepository.findDepartmentByDepartmentName(department).orElseThrow();
                /*get the tickets by status*/
                List<TicketAdminPresentationDTO> ticketsByStatus = ticketManagementService.getTicketsByDepartment(
                        passedDept);
                /*compose the response*/
                return ResponseEntity.ok().body(ticketsByStatus);
            }

            /*get the department*/
            User user = (User) authenticatedUser;
            Department dept = departmentRepository.findDepartmentByMembersContaining(user).orElseThrow();
            /*get the tickets*/
            List<TicketAdminPresentationDTO> ticketsByStatus = ticketManagementService.getTicketsByDepartment(
                    dept);
            /*respond*/
            return ResponseEntity.ok(ticketsByStatus);
        }catch (Exception e){
            /*log and respond*/
            log.error(e.getMessage());
        }
        return ResponseEntity.badRequest().build();
    }
}
