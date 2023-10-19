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
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
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

@OpenAPIDefinition(tags = {@Tag(name = "USER REGISTRATION API", description = "documentation of the exposed user APIs"),
@Tag(name = "USER LOGIN API", description = "the user login operations api")},
info = @Info(title = "USER API",description = "documentation of the exposed user APIs",summary = "the user api",
        contact = @Contact(name = "kamar baraka", email = "kamar254baraka@gmail.com"), version = "1.0.0.1"))
@RestController
@RequiredArgsConstructor
@RequestMapping(value = {"api/v1/users"})
public class UserRegistrationController {

    private final UserRegistrationService userRegistrationService;

    /**
     * register a user*/
    @PostMapping(value = {"register"})
    @Operation(tags = {"USER REGISTRATION API"}, summary = "register a user", description = "an api to register users")
//    @PreAuthorize("permitAll()")
    public ResponseEntity<EntityModel<DtoType>> registerUser(@Validated @RequestBody UserRegistrationDTO registrationDTO){

        /*register the user*/
        userRegistrationService.registerUser(registrationDTO);
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
    @Operation(tags = {"USER REGISTRATION API"}, summary = "activate a user", description = "api to activate a")
    /*@PreAuthorize("permitAll()")*/
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
            return ResponseEntity.status(HttpStatus.CONFLICT).body(response);
        }

        /*create message*/
        DtoType message = new ActivationSuccessDTO("activation successful");
        /*construct response*/
        EntityModel<DtoType> response = EntityModel.of(message);

        /*return response*/
        return ResponseEntity.ok(response);
    }

}
