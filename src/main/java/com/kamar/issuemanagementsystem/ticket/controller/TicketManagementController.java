package com.kamar.issuemanagementsystem.ticket.controller;

import com.kamar.issuemanagementsystem.attachment.entity.Attachment;
import com.kamar.issuemanagementsystem.attachment.utils.AttachmentUtilityService;
import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.ticket.data.TicketStatus;
import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import static java.util.stream.Collectors.toList;

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
    private final DepartmentRepository departmentRepository;
    private final UserAuthorityUtility userAuthorityUtility;
    private final AttachmentUtilityService attachmentUtilityService;
    private final UserUtilityService userUtilityService;

    @GetMapping(value = {"get"}, produces = {MediaType.APPLICATION_JSON_VALUE})
    @Operation(tags = {"Ticket Creation", "Ticket Assignment", "Ticket Submission", "Ticket Management"},
            summary = "get a ticket", description = "get a ticket by ID",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"AUTHENTICATED"})})
    @PreAuthorize("isAuthenticated()")
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

        /*filter for department admin*/
        if (userUtilityService.hasAuthority(userDetails, "department_admin"))
            return ResponseEntity.badRequest().body(
                            EntityModel.of(new InfoDTO("you are not permitted to access this resource")));

        if (userDetails.getAuthorities().contains(userAuthorityUtility.getFor("employee")) &&
                (!userDetails.getUsername().equals(ticket.getAssignedTo().getUsername()) &&
                    (!userDetails.getUsername().equals(ticket.getRaisedBy().getUsername()))))
                    {
                        return ResponseEntity.badRequest().body(
                            EntityModel.of(new InfoDTO("you are not permitted to access this resource"))
                    );

        }

        /*check authorities and perform actions based*/
        if (userUtilityService.hasAuthority(userDetails, "user")){
            /*return a response*/
            return ResponseEntity.badRequest().body(
                    EntityModel.of(new InfoDTO("operation not allowed"))
            );
        }

        /*return response*/
        return ResponseEntity.ok().body(response);
    }

    @GetMapping(value = {"attachment"}, produces = {MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @Operation(tags = {"Ticket Management"}, summary = "Api to download ticket attachment by ticket id",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"AUTHENTICATED"})})
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    public ResponseEntity<FileSystemResource> downloadTicketAttachments(@RequestParam("ticket_id") long id){

        List<Attachment> attachments;
        try {
            /*get the attachment*/
            attachments = ticketManagementService.downloadTicketAttachment(id);
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

            /*convert the attachment to file*/
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
    @Operation(tags = {"Ticket Management", "Department Management"}, summary = "Api to get tickets by status that belong to a department.",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"ADMIN", "DEPARTMENT_ADMIN"})})
    @RequestBody(content = {
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
            @Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    })
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEPARTMENT_ADMIN')")
    @CrossOrigin
    public ResponseEntity<List<? extends DtoType>> getTicketsByDepartmentAndStatus(@AuthenticationPrincipal User authenticatedUser,
                                                                   @RequestParam("department") String department,
                                                                   @RequestParam("status") String status){
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
            Department dept = departmentRepository.findDepartmentByMembersContaining(authenticatedUser).orElseThrow();
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
            @AuthenticationPrincipal User authenticatedUser,
            @RequestParam("department") String department
    ){

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
            Department dept = departmentRepository.findDepartmentByMembersContaining(authenticatedUser).orElseThrow();
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
