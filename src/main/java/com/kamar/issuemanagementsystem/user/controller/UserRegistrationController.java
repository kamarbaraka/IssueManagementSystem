package com.kamar.issuemanagementsystem.user.controller;

import com.kamar.issuemanagementsystem.user.data.dto.ActivationSuccessDTO;
import com.kamar.issuemanagementsystem.user.data.dto.DtoType;
import com.kamar.issuemanagementsystem.user.data.dto.UserActivationDTO;
import com.kamar.issuemanagementsystem.user.data.dto.UserRegistrationDTO;
import com.kamar.issuemanagementsystem.user.exceptions.UserException;
import com.kamar.issuemanagementsystem.user.service.UserRegistrationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
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
    @PostMapping(value = {"register"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_JSON_VALUE,
            MediaType.APPLICATION_FORM_URLENCODED_VALUE})
    @Operation(tags = {"User Registration"}, summary = "register a user. {'ADMIN', 'OWNER'}",
            description = "an api to register users.",
    security = {@SecurityRequirement(name = "basicAuth")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
            @Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE),
            @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE),
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    })
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
        Link activationLink = WebMvcLinkBuilder.linkTo(UserRegistrationController.class).withRel("activate");
        /*add link*/
        response.add(activationLink);
        /*return response*/
        return ResponseEntity.ok(response);
    }

    /**
     * activate a user*/
    @PostMapping(value = {"activate"}, consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE, MediaType.APPLICATION_JSON_VALUE,
    MediaType.MULTIPART_FORM_DATA_VALUE})
    @Operation(tags = {"User Activation"}, summary = "activate a user. {'ADMIN', 'OWNER'}", description = "api to activate a",
    security = {@SecurityRequirement(name = "basicAuth")})
    @io.swagger.v3.oas.annotations.parameters.RequestBody(content = {
            @Content(mediaType = MediaType.APPLICATION_FORM_URLENCODED_VALUE),
            @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE),
            @Content(mediaType = MediaType.APPLICATION_JSON_VALUE)
    })
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    @CrossOrigin
    public ResponseEntity<EntityModel<DtoType>> activateUser(@Validated @RequestParam(name = "username") @Email String username,
                                                             @RequestParam("activation_token") String activationToken){

        UserActivationDTO activationReq = new UserActivationDTO(username, activationToken);
        /*activate the user*/
        try {
            userRegistrationService.activateUser(activationReq);
        } catch (UserException e) {

            /*log*/
            log.warn(e.getMessage());
            /*construct message*/
            DtoType message = new ActivationSuccessDTO("invalid token");
            /*construct response*/
            EntityModel<DtoType> response = EntityModel.of(message);
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
