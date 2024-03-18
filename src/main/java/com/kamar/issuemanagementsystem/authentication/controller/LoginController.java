/*
package com.kamar.issuemanagementsystem.authentication.controller;

import com.kamar.issuemanagementsystem.user_management.data.dto.UserPresentationDTO;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import com.kamar.issuemanagementsystem.user_management.utility.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

*/
/**
 * the login controller.
 * @author kamar baraka.*//*



@RestController
@RequestMapping(value = {"api/v1/login"})
@RequiredArgsConstructor
public class LoginController {

    private final UserMapper userMapper;

    @GetMapping
    @Operation(tags = {"Authentication"},
    summary = "The login Api.",
    security = {@SecurityRequirement(name = "basicAuth", scopes = {"AUTHENTICATED"})})
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    public ResponseEntity<UserPresentationDTO> login(@AuthenticationPrincipal UserDetails userDetails){

        */
/*get the user*//*

        UserEntity userEntity = (UserEntity) userDetails;
        */
/*convert to dto*//*

        UserPresentationDTO userPresentationDTO = userMapper.userToPresentationDTO(userEntity);

        */
/*return details of user*//*

        return ResponseEntity.ok(userPresentationDTO);
    }
}
*/
