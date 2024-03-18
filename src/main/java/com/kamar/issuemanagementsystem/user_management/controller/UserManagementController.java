/*
package com.kamar.issuemanagementsystem.user_management.controller;

import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.user_management.data.dto.DtoType;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import com.kamar.issuemanagementsystem.user_management.exceptions.UserException;
import com.kamar.issuemanagementsystem.user_management.service.UserManagementService;
import com.kamar.issuemanagementsystem.user_management.utility.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.constraints.Email;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

*/
/**
 * user management api.
 * @author kamar baraka.*//*


@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/users/management"})
@Log4j2
public class UserManagementController {

    private final UserManagementService userManagementService;
    private final UserMapper userMapper;
    private final UserAuthorityUtility userAuthorityUtility;


    @GetMapping
    @Operation(tags = {"User Management", "User Reporting"}, summary = "get all users. {'ADMIN', 'OWNER'}",
    security = {@SecurityRequirement(name = "basicAuth")})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    @CrossOrigin
    public ResponseEntity<List<EntityModel<DtoType>>> getAllUsers(){

        List<UserEntity> allUserEntities;

        try
        {
            */
/*get all users*//*

            allUserEntities = userManagementService.getAllUsers();
        }catch (Exception e){

            */
/*log and respond*//*

            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        */
/*construct a response*//*

        List<EntityModel<DtoType>> usersDto = convertListToResponse(allUserEntities);

        */
/*return the response*//*

        return ResponseEntity.ok(usersDto);

    }

    @GetMapping(value = {"user"})
    @Operation(tags = {"User Management", "Ticket Assignment", "User Reporting"}, summary = "get a user by username.",
    security = {@SecurityRequirement(name = "basicAuth")})
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> getUserByUsername(@RequestParam("username") String username){


        UserDetails userDetails = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        */
/*get user by username*//*

        UserEntity userEntity;
        try {
            userEntity = userManagementService.getUserByUsername(username);
        } catch (Exception e) {

            */
/*log the exception*//*

            log.warn(e.getMessage());
            */
/*return status*//*

            return ResponseEntity.notFound().build();
        }
        */
/*map to dto*//*

        DtoType userDto = userMapper.userToPresentationDTO(userEntity);
        */
/*construct a response*//*

        EntityModel<DtoType> response = EntityModel.of(userDto);
        */
/*add links*//*


        */
/*check authority*//*

        if (userDetails.getAuthorities().contains(userAuthorityUtility.getFor("employee")) ||
                userDetails.getAuthorities().contains(userAuthorityUtility.getFor("user"))) {

            if (!userDetails.getUsername().equals(username))
                return ResponseEntity.badRequest().body(
                        EntityModel.of(
                                new InfoDTO("you are not permitted to access this info")
                        )
                );

            */
/*return response*//*

            return ResponseEntity.ok(response);
        }
        */
/*return the response*//*

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = {"authority"})
    @Operation(tags = {"User Management", "Ticket Assignment", "User Reporting"}, summary = "get users by their authority. {'ADMIN'}",
    security = {@SecurityRequirement(name = "basicAuth")})
    @PreAuthorize("hasAuthority('ADMIN')")
    @CrossOrigin
    public ResponseEntity<List<EntityModel<DtoType>>> getUsersByAuthority(@RequestParam("authority") String authority){

        List<UserEntity> userEntities;

        try
        {
            */
/*get users by authority*//*

            userEntities = userManagementService.getUsersByAuthority(userAuthorityUtility.getFor(authority));
        }catch (Exception e){

            */
/*log and respond*//*

            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        */
/*construct a response*//*

        List<EntityModel<DtoType>> listOfUsers = convertListToResponse(userEntities);

        */
/*return the response*//*

        return ResponseEntity.ok(listOfUsers);
    }

    private List<EntityModel<DtoType>> convertListToResponse(List<UserEntity> userEntityList){

        return userEntityList.stream().map(user -> {

            */
/*map the user to dto*//*

            DtoType userDto = userMapper.userToPresentationDTO(user);
            */
/*construct response*//*

            EntityModel<DtoType> response = EntityModel.of(userDto);
            */
/*add links*//*

            Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                            UserManagementController.class).getUserByUsername(user.getUsername()))
                    .withRel("user");

            response.add(userLink);

            */
/*return the response*//*

            return response;
        }).toList();
    }

    @PutMapping(value = {"elevate"}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Operation(tags = {"User Management", "Role Management"}, summary = "elevate a user authority. {'ADMIN', 'OWNER'}",
    security = {@SecurityRequirement(name = "basicAuth")})
    @RequestBody(content = {@Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE),
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> elevateUser(@RequestParam("authority") String authority,
                                                            @RequestParam("username") String username){

        */
/*elevate user*//*

        try {
            userManagementService.elevate(username,
                    userAuthorityUtility.getFor(authority));
        } catch (IllegalArgumentException | UserException e) {

            */
/*log the exception*//*

            log.warn(e.getMessage());

            */
/*return status*//*

            return ResponseEntity.badRequest().build();
        }

        */
/*construct a response*//*

        return ResponseEntity.ok(
                EntityModel.of(
                        new InfoDTO(username + " successfully elevated")
                )
        );
    }

    @GetMapping(value = {"downgrade"})
    @Operation(tags = {"User Management", "Role Management"}, summary = "Api to remove a user's role. {'ADMIN', 'OWNER'}",
            security = {@SecurityRequirement(name = "basicAuth")}
    )
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    @CrossOrigin
    public ResponseEntity<Void> downgradeUser(@RequestParam("username") @Validated @Email String username,
                                              @RequestParam("role_to_remove") String role){

        */
/*downgrade the user*//*

        try {
            userManagementService.downgrade(username, role);
        } catch (UserException e) {
            */
/*log and respond*//*

            log.warn(e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        return ResponseEntity.ok().build();
    }

}
*/
