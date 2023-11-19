package com.kamar.issuemanagementsystem.authentication.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * the login controller.
 * @author kamar baraka.*/


@RestController
@RequestMapping(value = {"api/v1/login"})
public class LoginController {


    @GetMapping
    @Operation(tags = {"Authentication"},
    summary = "The login Api.",
    security = {@SecurityRequirement(name = "basicAuth")})
    @PreAuthorize("isAuthenticated()")
    @CrossOrigin
    public void login(){

        /*do nothing, get token*/
    }
}
