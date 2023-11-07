package com.kamar.issuemanagementsystem.analysis.controller;

import com.kamar.issuemanagementsystem.analysis.exception.AnalysisException;
import com.kamar.issuemanagementsystem.analysis.service.DepartmentAnalysisService;
import com.kamar.issuemanagementsystem.department.controller.DepartmentManagementController;
import com.kamar.issuemanagementsystem.department.data.DepartmentDto;
import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.user.controller.UserManagementController;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import com.kamar.issuemanagementsystem.user.data.dto.UserPresentationDTO;
import com.kamar.issuemanagementsystem.user.service.UserManagementServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * the department analysis controller.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/analysis/departments"})
@Log4j2
public class DepartmentAnalysisController {

    private final DepartmentAnalysisService departmentAnalysisService;


    @GetMapping(value = {"mostPerformant"})
    @Operation(
            tags = {"Department Analysis", "Department Reporting"},
            summary = "Api to get the most performant department",
            security = {@SecurityRequirement(name = "basicAuth")}
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found")
            }
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public ResponseEntity<EntityModel<DtoType>> mostPerformantDepartment(){

        DepartmentDto mostPerformantDept;

        /*get the most performant department*/
        try {
            mostPerformantDept = departmentAnalysisService.getMostPerformantDepartment();

        } catch (AnalysisException e) {

            /*log and respond*/
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        /*compose the response*/
        Link departmentLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                DepartmentManagementController.class).getDepartmentByName(mostPerformantDept.departmentName()))
                .withRel("department");

        return ResponseEntity.ok(
                EntityModel.of(
                        mostPerformantDept,
                        departmentLink
                )
        );
    }


    @GetMapping(value = {"bestPerformant"})
    @Operation(
            tags = {"Department Analysis", "Department Reporting"},
            summary = "Api to get the best performant department",
            security = {
                    @SecurityRequirement(name = "basicAuth")
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found")
            }
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public ResponseEntity<EntityModel<DtoType>> bestPerformantDepartment(){

        DepartmentDto bestPerformantDept;

        /*get the most performant department*/
        try {
            bestPerformantDept = departmentAnalysisService.getBestPerformantDepartment();

        } catch (AnalysisException e) {

            /*compose a response*/
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        /*compose the response*/
        Link departmentLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                DepartmentManagementController.class).getDepartmentByName(bestPerformantDept.departmentName()))
                .withRel("department");

        return ResponseEntity.ok(
                EntityModel.of(
                        bestPerformantDept,
                        departmentLink
                )
        );
    }

    @GetMapping(value = {"{department_name}/bestPerformant"})
    @Operation(
            tags = {"Department Analysis", "Department Reporting", "Employee Analysis", "User Analysis", "User Reporting"},
            summary = "Api to get the best performant employee in a department",
            security = {
                    @SecurityRequirement(name = "basicAuth")
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found")
            }
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public ResponseEntity<EntityModel<DtoType>> bestEmployeeInDepartment(@PathVariable("department_name") String departmentName,
                                                                         @AuthenticationPrincipal UserDetails userDetails){

        /*get department by the name*/
        UserPresentationDTO bestEmployee;
        try {
            bestEmployee = departmentAnalysisService.getBestEmployeeInDepartment(departmentName);
        } catch (AnalysisException e) {

            /*log and respond*/
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        /*compose*/
        Link userLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserManagementController.class)
                        .getUserByUsername(bestEmployee.username(), userDetails)).withRel("user");

        return ResponseEntity.ok(
                EntityModel.of(
                        bestEmployee,
                        userLink
                )
        );
    }

    @GetMapping(value = {"{department_name}/mostPerformant"})
    @Operation(
            tags = {"Department Analysis", "Department Reporting", "Employee Analysis", "User Analysis", "User Reporting"},
            summary = "Api to get the most performant employee in a department",
            security = {
                    @SecurityRequirement(name = "basicAuth")
            }
    )
    @ApiResponses(
            value = {
                    @ApiResponse(responseCode = "200", description = "success"),
                    @ApiResponse(responseCode = "404", description = "not found")
            }
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public ResponseEntity<EntityModel<DtoType>> mostPerformantEmployeeInDepartment(@PathVariable("department_name") String departmentName,
                                                                         @AuthenticationPrincipal UserDetails userDetails){

        /*get department by the name*/
        UserPresentationDTO mostPerformantEmpl;
        try {
            mostPerformantEmpl = departmentAnalysisService.getMostPerformantEmployeeInDepartment(departmentName);
        } catch (AnalysisException e) {

            /*log and respond*/
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        /*compose*/
        Link userLink = WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(UserManagementController.class)
                        .getUserByUsername(mostPerformantEmpl.username(), userDetails)).withRel("user");

        return ResponseEntity.ok(
                EntityModel.of(
                        mostPerformantEmpl,
                        userLink
                )
        );
    }
}
