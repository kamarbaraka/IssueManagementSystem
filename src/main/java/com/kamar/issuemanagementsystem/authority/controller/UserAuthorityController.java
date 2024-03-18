/*
package com.kamar.issuemanagementsystem.authority.controller;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.exception.AuthorityException;
import com.kamar.issuemanagementsystem.authority.service.UserAuthorityManagementService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

*/
/**
 * the user authority controller.
 * @author kamar baraka.*//*


@RestController
@RequestMapping(value = {"api/v1/authority"})
@RequiredArgsConstructor
@Log4j2
public class UserAuthorityController {

    private final UserAuthorityManagementService userAuthorityManagementService;

    @PostMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(tags = {"Role Management"}, summary = "Api to create a role",
            security = {@SecurityRequirement(name = "basicAuth")}
    )
    @RequestBody(content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
            @Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    @CrossOrigin
    public ResponseEntity<Void> createRole(@RequestParam("role_name") String authority){

        */
/*create a role*//*

        try {
            userAuthorityManagementService.createAuthority(authority);
        } catch (Exception e) {
            */
/*log and respond*//*

            log.error(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping(consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(tags = {"Role Management"}, summary = "Api to get a role by role name.",
            security = {@SecurityRequirement(name = "basicAuth")}
    )
    @RequestBody(content = {@Content(mediaType = MediaType.APPLICATION_JSON_VALUE),
            @Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE)})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    @CrossOrigin
    public ResponseEntity<UserAuthority> getRoleByName(@RequestParam("role") String authority){

        */
/*get the role*//*

        UserAuthority userAuthority;
        try {
           userAuthority  = userAuthorityManagementService.getAuthority(authority);
        } catch (AuthorityException e) {
            */
/*log and respond*//*

            log.warn(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(userAuthority);
    }

    @GetMapping(value = {"all"})
    @Operation(tags = {"Role Management"}, summary = "Api to get all the roles in the system.",
            security = {@SecurityRequirement(name = "basicAuth")}
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    @CrossOrigin
    public ResponseEntity<List<UserAuthority>> getAllAuthorities(){

        */
/*get all roles*//*

        List<UserAuthority> allAuthorities = userAuthorityManagementService.getAllAuthorities();

        return ResponseEntity.ok(allAuthorities);
    }
}
*/
