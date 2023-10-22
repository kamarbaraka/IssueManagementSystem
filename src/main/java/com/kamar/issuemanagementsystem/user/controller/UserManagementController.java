package com.kamar.issuemanagementsystem.user.controller;

import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import com.kamar.issuemanagementsystem.user.data.dto.UserPresentationDTO;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.service.UserManagementService;
import com.kamar.issuemanagementsystem.user.utility.mappers.UserMapper;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * user management api.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/users/management"})
public class UserManagementController {

    private final UserManagementService userManagementService;
    private final UserMapper userMapper;


    @GetMapping
    @Operation(tags = {"User Management"}, summary = "get all users")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<EntityModel<DtoType>>> getAllUsers(@AuthenticationPrincipal UserDetails userDetails){

        /*get all users*/
        List<User> allUsers = userManagementService.getAllUsers();

        /*construct a response*/
        List<EntityModel<DtoType>> usersDto = convertListToResponse(allUsers, userDetails);

        /*return the response*/
        return ResponseEntity.ok(usersDto);

    }

    @GetMapping(value = {"{username}"})
    @Operation(tags = {"User Management", "Ticket Assignment"}, summary = "get a user by username")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'EMPLOYEE')")
    public ResponseEntity<EntityModel<DtoType>> getUserByUsername(@PathVariable("username") String username,
                                                                  @AuthenticationPrincipal UserDetails userDetails){

        /*get user by username*/
        User user = userManagementService.getUserByUsername(username);
        /*map to dto*/
        DtoType userDto = userMapper.userToPresentationDTO(user);
        /*construct a response*/
        EntityModel<DtoType> response = EntityModel.of(userDto);
        /*add links*/

        /*check authority*/
        if (userDetails.getAuthorities().contains(Authority.EMPLOYEE))
            return ResponseEntity.ok(
                    EntityModel.of(
                            new InfoDTO("you are not permitted to access this info")
                    )
            );
        /*return the response*/
        return ResponseEntity.ok(response);
    }

    @GetMapping(value = {"authority/{authority}"})
    @Operation(tags = {"User Management", "Ticket Assignment"}, summary = "get users by their authority")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<List<EntityModel<DtoType>>> getUsersByAuthority(@PathVariable("authority") String authority,
                                                                          @AuthenticationPrincipal UserDetails userDetails){

        /*get users by authority*/
        List<User> users = userManagementService.getUsersByAuthority(Authority.valueOf(authority));

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

    @PatchMapping(value = {"elevate/{authority}/{username}"})
    @Operation(tags = {"User Management"}, summary = "elevate a user authority")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<EntityModel<DtoType>> elevateUser(@PathVariable("authority") String authority,
                                                            @PathVariable("username") String username){

        /*elevate user*/
        userManagementService.elevate(username,
                Authority.valueOf(authority));

        /*construct a response*/
        return ResponseEntity.ok(
                EntityModel.of(
                        new InfoDTO(username + " successfully elevated")
                )
        );
    }

}
