package com.kamar.issuemanagementsystem.user.controller;

import com.kamar.issuemanagementsystem.user.data.dto.ActivationSuccessDTO;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import com.kamar.issuemanagementsystem.user.data.dto.UserActivationDTO;
import com.kamar.issuemanagementsystem.user.data.dto.UserRegistrationDTO;
import com.kamar.issuemanagementsystem.user.exceptions.UserException;
import com.kamar.issuemanagementsystem.user.service.UserRegistrationService;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * the user controller.
 * @author kamar baraka.*/

@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/users/registration"})
@Log4j2
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    /**
     * register a user*/
    @PostMapping(value = {"register"})
    @Operation(tags = {"User Registration"}, summary = "register a user", description = "an api to register users",
    security = {@SecurityRequirement(name = "basicAuth")})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> registerUser(@Validated @RequestBody UserRegistrationDTO registrationDTO){

        /*register the user*/
        try {
            userRegistrationService.registerUser(registrationDTO);
        } catch (Exception e) {

            /*log the exception*/
            log.warn(e.getMessage());

            /*return status*/
            return ResponseEntity.badRequest().build();
        }
        /*create a response*/
        DtoType message = new ActivationSuccessDTO("registration successful, follow the link to activate.");
        EntityModel<DtoType> response = EntityModel.of(message);
        /*create link*/
        Link activationLink = WebMvcLinkBuilder.linkTo(UserRegistrationController.class)
                .slash("activate").slash(registrationDTO.username()).withRel("activate");
        /*add link*/
        response.add(activationLink);
        /*return response*/
        return ResponseEntity.status(201).body(response);
    }

    /**
     * activate a user*/
    @PostMapping(value = {"activate/{username}"})
    @Operation(tags = {"User Activation"}, summary = "activate a user", description = "api to activate a",
    security = {@SecurityRequirement(name = "basicAuth")})
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> activateUser(@PathVariable(name = "username") String username,
                                                             @RequestBody ActivationSuccessDTO activationDTO){
        UserActivationDTO activationReq = new UserActivationDTO(username, activationDTO.message());
        /*activate the user*/
        try {
            userRegistrationService.activateUser(activationReq);
        } catch (UserException e) {
            /*construct message*/
            DtoType message = new ActivationSuccessDTO("invalid token");
            /*construct response*/
            EntityModel<DtoType> response = EntityModel.of(message);
            /*create link*/
            Link selfLink = WebMvcLinkBuilder.linkTo(UserRegistrationController.class).slash("activate")
                    .slash(username).withSelfRel();
            response.add(selfLink);
            /*return*/
            return ResponseEntity.badRequest().body(response);
        }

        /*create message*/
        DtoType message = new ActivationSuccessDTO("activation successful");
        /*construct response*/
        EntityModel<DtoType> response = EntityModel.of(message);

        /*return response*/
        return ResponseEntity.ok(response);
    }

}
