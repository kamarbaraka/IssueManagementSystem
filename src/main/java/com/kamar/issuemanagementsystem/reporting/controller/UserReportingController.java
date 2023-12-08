package com.kamar.issuemanagementsystem.reporting.controller;

import com.kamar.issuemanagementsystem.reporting.service.UserReportingService;
import com.kamar.issuemanagementsystem.user.entity.User;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * controller to handle user reporting.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@Log4j2
public class UserReportingController {

    private final UserReportingService userReportingService;


    /**
     * Returns an array of usernames for employees who are not assigned to a department.
     *
     * This method retrieves the employees who are not assigned to a department from the UserReportingService.
     * It then maps the usernames of these employees to an array of strings. The array of usernames is returned in a ResponseEntity object.
     *
     * @return a ResponseEntity object containing an array of Strings representing the usernames of employees not in a department
     */
    @GetMapping(value = {"api/v1/users/report/nonDeptEmps"})
    @Operation(
            tags = {"User Reporting"},
            summary = " {'ADMIN', 'DEPARTMENT_ADMIN'}",
            security = {@SecurityRequirement(name = "basicAuth")}
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'DEPARTMENT_ADMIN')")
    @CrossOrigin
    public ResponseEntity<String[]> getEmployeesNotInDept(){

        /*get the employees not in department*/
        List<User> employeesNotInDept = userReportingService.getEmployeesNotInDept();
        /*get the usernames*/
        String[] arrayOfEmployees = employeesNotInDept.stream().map(User::getUsername).toList().toArray(new String[0]);
        /*return*/
        return ResponseEntity.ok(arrayOfEmployees);

    }
}
