package com.kamar.issuemanagementsystem.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * controller for user utilities.
 * @author kamar baraka.*/


@RestController
@RequestMapping(value = {"api/users/util"})
public class UserUtilitiesController {


    @GetMapping(value = {"", "who"})
    @Operation(tags = {"Utilities"}, summary = "api to discover the authenticated user")
    public ResponseEntity<UserDetails> whoAmI(@AuthenticationPrincipal UserDetails user){

        return ResponseEntity.ok(user);
    }
}
