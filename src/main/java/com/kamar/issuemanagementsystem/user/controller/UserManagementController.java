package com.kamar.issuemanagementsystem.user.controller;

import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import com.kamar.issuemanagementsystem.user.data.dto.UserPresentationDTO;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.service.UserManagementService;
import com.kamar.issuemanagementsystem.user.utility.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.List;

/**
 * user management api.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/users/management"})
@Log4j2
public class UserManagementController {

    private final UserManagementService userManagementService;
    private final UserMapper userMapper;


    @GetMapping
    @Operation(tags = {"User Management", "User Reporting"}, summary = "get all users",
    security = {@SecurityRequirement(name = "basicAuth")})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    @CrossOrigin
    public ResponseEntity<List<EntityModel<DtoType>>> getAllUsers(@AuthenticationPrincipal UserDetails userDetails){

        List<User> allUsers;

        try
        {
            /*get all users*/
            allUsers = userManagementService.getAllUsers();
        }catch (Exception e){

            /*log and respond*/
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        /*construct a response*/
        List<EntityModel<DtoType>> usersDto = convertListToResponse(allUsers, userDetails);

        /*return the response*/
        return ResponseEntity.ok(usersDto);

    }

    @GetMapping(value = {"user"})
    @Operation(tags = {"User Management", "Ticket Assignment", "User Reporting"}, summary = "get a user by username",
    security = {@SecurityRequirement(name = "basicAuth")})
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> getUserByUsername(@RequestParam("username") String username,
                                                                  @AuthenticationPrincipal UserDetails userDetails){

        /*get user by username*/
        User user;
        try {
            user = userManagementService.getUserByUsername(username);
        } catch (Exception e) {

            /*log the exception*/
            log.warn(e.getMessage());
            /*return status*/
            return ResponseEntity.notFound().build();
        }
        /*map to dto*/
        DtoType userDto = userMapper.userToPresentationDTO(user);
        /*construct a response*/
        EntityModel<DtoType> response = EntityModel.of(userDto);
        /*add links*/

        /*check authority*/
        if (userDetails.getAuthorities().contains(Authority.EMPLOYEE) || userDetails.getAuthorities().contains(Authority.USER)) {

            if (!userDetails.getUsername().equals(username))
                return ResponseEntity.badRequest().body(
                        EntityModel.of(
                                new InfoDTO("you are not permitted to access this info")
                        )
                );

            /*return response*/
            return ResponseEntity.ok(response);
        }
        /*return the response*/
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = {"authority"})
    @Operation(tags = {"User Management", "Ticket Assignment", "User Reporting"}, summary = "get users by their authority",
    security = {@SecurityRequirement(name = "basicAuth")})
    @PreAuthorize("hasAuthority('ADMIN')")
    @CrossOrigin
    public ResponseEntity<List<EntityModel<DtoType>>> getUsersByAuthority(@RequestParam("authority") String authority,
                                                                          @AuthenticationPrincipal UserDetails userDetails){

        List<User> users;

        try
        {
            /*get users by authority*/
            users = userManagementService.getUsersByAuthority(Authority.valueOf(authority.toUpperCase()));
        }catch (Exception e){

            /*log and respond*/
            log.error(e.getMessage());
            return ResponseEntity.notFound().build();
        }

        /*construct a response*/
        List<EntityModel<DtoType>> listOfUsers = convertListToResponse(users, userDetails);

        /*return the response*/
        return ResponseEntity.ok(listOfUsers);
    }

    private List<EntityModel<DtoType>> convertListToResponse(List<User> userList, UserDetails userDetails){

        return userList.stream().map(user -> {

            /*map the user to dto*/
            DtoType userDto = userMapper.userToPresentationDTO(user);
            /*construct response*/
            EntityModel<DtoType> response = EntityModel.of(userDto);
            /*add links*/
            Link userLink = WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(
                            UserManagementController.class).getUserByUsername(user.getUsername(), userDetails))
                    .withRel("user");

            response.add(userLink);

            /*return the response*/
            return response;
        }).toList();
    }

    @PutMapping(value = {"elevate"}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE})
    @Operation(tags = {"User Management"}, summary = "elevate a user authority",
    security = {@SecurityRequirement(name = "basicAuth")})
    @RequestBody(content = {@Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE),
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> elevateUser(@RequestParam("authority") String authority,
                                                            @RequestParam("username") String username){

        /*elevate user*/
        try {
            userManagementService.elevate(username,
                    Authority.valueOf(authority.toUpperCase()));
        } catch (IllegalArgumentException e) {

            /*log the exception*/
            log.warn(e.getMessage());

            /*return status*/
            return ResponseEntity.badRequest().build();
        }

        /*construct a response*/
        return ResponseEntity.ok(
                EntityModel.of(
                        new InfoDTO(username + " successfully elevated")
                )
        );
    }

}
