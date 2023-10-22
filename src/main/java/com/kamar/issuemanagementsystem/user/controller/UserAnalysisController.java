package com.kamar.issuemanagementsystem.user.controller;

import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import com.kamar.issuemanagementsystem.user.data.dto.UserPresentationDTO;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.service.UserAnalysisService;
import com.kamar.issuemanagementsystem.user.utility.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
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
@RequestMapping(value = {"api/users/analysis"})
public class UserAnalysisController {

    private final UserAnalysisService userAnalysisService;
    private final UserMapper userMapper;

    @GetMapping(value = {"employeePerformance"})
    @Operation(tags = {"Employee Analysis"}, summary = "get the most performant employee")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EntityModel<DtoType>> getMostPerformantEmployee(){

        /*get the most performant employee*/
        User mostPerformantEmployee = userAnalysisService.mostPerformantEmployee();
        /*map to dto*/
        DtoType employeeDto = userMapper.userToPresentationDTO(mostPerformantEmployee);
        /*construct a response*/
        EntityModel<DtoType> response = EntityModel.of(employeeDto);
        /*add links*/

        /*return response*/
        return ResponseEntity.ok(response);
    }
}
