package com.kamar.issuemanagementsystem.client_management.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.OAuth2TokenFormat;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;

import java.time.Duration;
import java.time.Instant;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * The RegisteredClientEntity class represents a registered client in the system.
 * A registered client is an entity that can access protected resources using OAuth 2.0 authorization.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity(name = "registered_clients")
public class RegisteredClientEntity {

    @Id
    @Column(name = "id", nullable = false, unique = true)
    private final String  id = UUID.randomUUID().toString();

    @NotBlank(message = "client name cannot be blank.")
    @NotEmpty(message = "client name cannot be empty.")
    @NotNull(message = "client name cannot be null.")
    @Column(name = "client_name", nullable = false)
    private String clientName;

    @Builder.Default
    @NotBlank(message = "client ID cannot be blank.")
    @NotEmpty(message = "client ID cannot be empty.")
    @NotNull(message = "client ID cannot be null.")
    @Column(name = "client_id", nullable = false, unique = true)
    private String clientId = UUID.randomUUID().toString();

    @Builder.Default
    @NotBlank(message = "client secret cannot be blank.")
    @NotEmpty(message = "client secret cannot be empty.")
    @NotNull(message = "client secret cannot be null.")
    @Column(name = "client_secret", nullable = false)
    private String clientSecret = UUID.randomUUID().toString();

    @Builder.Default
    @NotNull(message = "client issued at cannot be null.")
    @Column(nullable = false)
    private final Instant clientIssuedAt = Instant.now();

    private Instant clientSecretExpiresAt;

    @NotBlank(message = "redirect uri cannot be blank.")
    @NotEmpty(message = "redirect uri cannot be empty.")
    @NotNull(message = "redirect uri cannot be null.")
    @Column(nullable = false)
    private String redirectUri;

    @NotBlank(message = "post logout redirect uri cannot be blank.")
    @NotEmpty(message = "post logout redirect uri cannot be empty.")
    @NotNull(message = "post logout redirect uri cannot be null.")
    @Column(nullable = false)
    private String postLogoutRedirectUri;

    @NotBlank(message = "scopes cannot be blank.")
    @NotEmpty(message = "scopes cannot be empty.")
    @NotNull(message = "scopes cannot be null.")
    @Builder.Default
    @ElementCollection
    @Column(name = "scope", nullable = false)
    @CollectionTable(name = "registered_clients_scopes", joinColumns = @JoinColumn(name = "registred_client_id"))
    private  Set<String> scopes = new LinkedHashSet<>(
            List.of(OidcScopes.OPENID, OidcScopes.PROFILE, OidcScopes.EMAIL)
    );

    @NotNull(message = "client authentication method cannot be null")
    @ElementCollection
    @Column(name = "client_authentication_method", nullable = false)
    @CollectionTable(name = "registered_clients_clientAuthenticationMethods", joinColumns = @JoinColumn(name = "owner_id"))
    private final Set<ClientAuthenticationMethod> clientAuthenticationMethods = new LinkedHashSet<>(
            List.of(
                    ClientAuthenticationMethod.CLIENT_SECRET_BASIC,
                    ClientAuthenticationMethod.CLIENT_SECRET_POST
            )
    );

    @Builder.Default
    @NotNull(message = "authorization grant type cannot be null.")
    @ElementCollection
    @Column(name = "authorization_grant_type", nullable = false)
    @CollectionTable(name = "registered_clients_authorizationGrantTypes", joinColumns = @JoinColumn(name = "registered_client_id"))
    private Set<AuthorizationGrantType> authorizationGrantTypes = new LinkedHashSet<>();

    @Builder.Default
    @NotNull(message = "client settings cannot be null.")
    @Column(name = "client_settings", nullable = false)
    private final ClientSettings clientSettings = configureClientSettings();

    @Builder.Default
    @NotNull(message = "token settings cannot be null.")
    @Column(name = "token_settings", nullable = false)
    private final TokenSettings tokenSettings = configureTokenSettings();

    @Builder.Default
    @NotNull(message = "updated on cannot be null.")
    @Column(nullable = false)
    private Instant updatedOn = Instant.now();

    /**
     * Configures the client settings for authorization consent.
     *
     * @return The configured ClientSettings object with authorization consent enabled.
     */
    private ClientSettings configureClientSettings(){

        /*enable authorization consent*/
        return ClientSettings
                .builder()
                .requireAuthorizationConsent(true)
                .build();
    }

    /**
     * Configures the token settings for token generation and validation.
     *
     * @return The configured TokenSettings object.
     */
    private TokenSettings configureTokenSettings(){

        /*set the duration for the tokens to live*/
        Duration duration = Duration.ofMinutes(10);
        /*configure the settings*/
        return TokenSettings
                .builder()
                .accessTokenFormat(OAuth2TokenFormat.SELF_CONTAINED)
                .accessTokenTimeToLive(duration)
                .authorizationCodeTimeToLive(duration)
                .deviceCodeTimeToLive(duration)
                .idTokenSignatureAlgorithm(SignatureAlgorithm.ES512)
                .build();
    }

}