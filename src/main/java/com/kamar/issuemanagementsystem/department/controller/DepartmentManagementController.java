package com.kamar.issuemanagementsystem.department.controller;

import com.google.api.client.auth.openidconnect.IdToken;
import com.kamar.issuemanagementsystem.department.data.AddUserToDepartmentDTO;
import com.kamar.issuemanagementsystem.department.data.DepartmentCreationDto;
import com.kamar.issuemanagementsystem.department.data.DepartmentDtoType;
import com.kamar.issuemanagementsystem.department.exception.DepartmentException;
import com.kamar.issuemanagementsystem.department.service.DepartmentManagementService;
import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * the department management controller.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/departments"})
@Log4j2
public class DepartmentManagementController {

    private final DepartmentManagementService departmentManagementService;

    @PostMapping
    @Operation(tags = {"Department Creation", "Department Management"}, summary = "api to create a department",
            security = {@SecurityRequirement(name = "basicAuth")})
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "successfully created")
    })
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public ResponseEntity<EntityModel<DtoType>> createDepartment(@RequestBody DepartmentCreationDto departmentCreationDto){

        /*create the department*/
        try {
            departmentManagementService.createDepartment(departmentCreationDto);
        } catch (DepartmentException e) {

            /*log and respond*/
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        /*construct the response*/
        return ResponseEntity.status(201).body(
                EntityModel.of(
                        new InfoDTO("department created successfully"),
                        WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                                        DepartmentManagementController.class).
                                getDepartmentByName(departmentCreationDto.departmentName())).withRel("department")
                )
        );
    }


    @GetMapping(value = {"{department_name}"})
    @Operation(
            tags = {"Department Management", "Department Report"}, summary = "api to get department by name",
            security = {
                    @SecurityRequirement(name = "basicAuth")}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "404", description = "the department is not found"),
                    @ApiResponse(responseCode = "200", description = "successfully fetched the department")
            }
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public ResponseEntity<EntityModel<DtoType>> getDepartmentByName(@PathVariable("department_name") String departmentName){

        /*get the department*/
        DepartmentDtoType department;

        try {
            department = departmentManagementService.getDepartmentByName(departmentName);
        } catch (DepartmentException e) {

            /*construct response*/
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(EntityModel.of(department));
    }

    @PostMapping(value = {"add"})
    @Operation(
            tags = {"Department Management", "User Management"},
            summary = "Api to add users to a department",
            responses = {
                    @ApiResponse(responseCode = "200", description = "users have been successfully added to department"),
            },
            security = {@SecurityRequirement(name = "basicAuth", scopes = {"read:add"})}
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Object> addUsersToDepartment(@Validated @RequestBody AddUserToDepartmentDTO addUserToDepartmentDTO){

        /*add users to department*/
        try {
            departmentManagementService.addUsersToDepartment(addUserToDepartmentDTO);
        } catch (DepartmentException e) {

            /*log and respond*/
            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }
}
