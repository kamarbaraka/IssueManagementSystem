package com.kamar.issuemanagementsystem.analysis.controller;

import com.kamar.issuemanagementsystem.analysis.exception.AnalysisException;
import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import com.kamar.issuemanagementsystem.user.data.dto.UserPresentationDTO;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.analysis.service.UserAnalysisService;
import com.kamar.issuemanagementsystem.user.utility.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * user analysis controller.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/analysis/users"})
public class UserAnalysisController {

    private final UserAnalysisService userAnalysisService;
    private final UserMapper userMapper;

    @GetMapping(value = {"employeePerformance"})
    @Operation(tags = {"Employee Analysis", "User Analysis", "User Reporting"},
            summary = "get the most performant employee")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public ResponseEntity<EntityModel<DtoType>> getMostPerformantEmployee(){

        /*get the most performant employee*/
        UserPresentationDTO mostPerformantEmployee;
        try {
            mostPerformantEmployee = userAnalysisService.bestPerformantEmployee();
        } catch (AnalysisException e) {

            /*notify*/
            return ResponseEntity.status(HttpStatus.CONFLICT).body(
                    EntityModel.of(
                            new InfoDTO(e.getMessage())
                    )
            );
        }

        /*construct a response*/
        EntityModel<DtoType> response = EntityModel.of(mostPerformantEmployee);
        /*add links*/

        /*return response*/
        return ResponseEntity.ok(response);
    }
}
