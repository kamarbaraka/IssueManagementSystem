package com.kamar.issuemanagementsystem.authentication.controller;

import com.kamar.issuemanagementsystem.ticket.data.dto.InfoDTO;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller to perform logout functions.
 * @author kamar baraka.*/

@RestController
@RequestMapping(value = {"logout"})
public class LogoutController {

    @GetMapping
    @Operation(tags = {"Authentication"}, summary = "logout a user")
    public void logout(HttpServletRequest request, HttpServletResponse response,
                                          Authentication authentication){

        /*check if the user is logged in*/
        if (authentication.isAuthenticated()) {

            new SecurityContextLogoutHandler().logout(request, response, authentication);
        }

    }
}
