package com.kamar.issuemanagementsystem.api_doc;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.OAuthFlow;
import io.swagger.v3.oas.annotations.security.OAuthFlows;
import io.swagger.v3.oas.annotations.security.OAuthScope;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.web.bind.annotation.RestController;

/**
 * the Api definition of the ticket controller.
 * @author kamar baraka.*/


@RestController
@OpenAPIDefinition(
        tags = {
                @Tag(name = "Utilities", description = "Useful apis"),
                @Tag(name = "Authentication", description = "Apis for authentication functionalities"),
                @Tag(name = "Role Management", description = "Apis to create,get,update,delete roles"),
                @Tag(name = "User Registration", description = "Apis to register a user"),
                @Tag(name = "User Activation", description = "Apis to activate a user"),
                @Tag(name = "User Management", description = "Apis to manage a user"),
                @Tag(name = "User Analysis", description = "Apis for user analysis"),
                @Tag(name = "User Reporting", description = "Apis for generating user reports"),
                @Tag(name = "Department Creation", description = "Apis for creation of department"),
                @Tag(name = "Department Management", description = "Apis for department management"),
                @Tag(name = "Department Analysis", description = "Apis for department analysis"),
                @Tag(name = "Department Reporting", description = "Apis for department reporting"),
                @Tag(name = "Ticket Creation", description = "List of APIs for ticket creation"),
                @Tag(name = "Ticket Assignment", description = "List of Apis for ticket assignment"),
                @Tag(name = "Ticket Referral", description = "Refer a ticket"),
                @Tag(name = "Ticket Submission", description = "Apis for ticket submission"),
                @Tag(name = "Ticket Management", description = "Apis to manage a ticket"),
                @Tag(name = "Ticket Feedback", description = "Api to send a feedback on a ticket"),
                @Tag(name = "Ticket Reporting", description = "Apis to report on tickets"),
                @Tag(name = "Ticket Analysis", description = "Apis to analyse tickets"),
        },
        info = @Info(
                title = "Ticket Management System Api Documentation",
                description = "The documentation for the `` ticket management system`` Apis",
                version = "v1.0.4",
                contact = @Contact(
                        name = "kamar baraka",
                        email = "kamar254baraka@gmail.com",
                        url = "https://github.com/kamarbaraka"

                )

        )
)


@SecurityScheme(
        name = "openID",
        type = SecuritySchemeType.OPENIDCONNECT,
        description = "Authentication and authorization via the open ID specification.",
        openIdConnectUrl = "https://example.com/.well-known/openid-configuration"
)

@SecurityScheme(
        type = SecuritySchemeType.OAUTH2,
        name = "oauth2",
        description = "security scheme for oauth2 authentication.",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        flows = @OAuthFlows(
                authorizationCode = @OAuthFlow(
                        authorizationUrl = "http://localhost:9080/oauth2/authorize",
                        tokenUrl = "http://localhost:9080/oauth2/token",
                        refreshUrl = "http://localhost:9080/oauth2/token",
                        scopes = {
                                @OAuthScope(name = OidcScopes.OPENID, description = "."),
                                @OAuthScope(name = OidcScopes.PROFILE,description = "get the user's info."),
                                @OAuthScope(name = OidcScopes.EMAIL, description = "get the email of the user.")
                        }
                )
        )
)

public class TicketApiDoc {

}
