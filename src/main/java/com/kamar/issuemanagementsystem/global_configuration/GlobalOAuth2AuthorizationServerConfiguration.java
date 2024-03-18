package com.kamar.issuemanagementsystem.global_configuration;

import com.kamar.issuemanagementsystem.client_management.repositories.RegisteredClientEntityRepository;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jose.jws.SignatureAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.JdbcOAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationConsentService;
import org.springframework.security.oauth2.server.authorization.OAuth2AuthorizationService;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OidcConfigurer;
import org.springframework.security.oauth2.server.authorization.oidc.OidcProviderConfiguration;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;

import javax.sql.DataSource;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * Configuration class for the global OAuth2 Authorization Server.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Configuration
@RequiredArgsConstructor
public class GlobalOAuth2AuthorizationServerConfiguration {


    private final DataSource dataSource;
    private final RegisteredClientRepository repository;

    /**
     * Configures the security filter chain for the authorization server.
     *
     * @param httpSecurity The HttpSecurity object used to configure the filter chain.
     * @return The configured SecurityFilterChain instance.
     * @throws Exception If an error occurs while configuring the filter chain.
     */
    @Bean
    @Order(1)
    public SecurityFilterChain authorizationServerSecurityFilterChain(HttpSecurity httpSecurity)throws Exception{

        /*apply configurations to the filter chain*/
        OAuth2AuthorizationServerConfiguration.applyDefaultSecurity(httpSecurity);
        /*customize configuration and enable open id connect */
        httpSecurity
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults())
                .oidc(configureOidc());
        /*build and return*/
        return httpSecurity.build();
    }

    /**
     * Configures the OpenID Connect (OIDC) provider for the OAuth2 authorization server.
     *
     * @return The customizer for the OIDC configuration.
     */
    private Customizer<OidcConfigurer> configureOidc(){

        /*configure oidc*/
        return oidc -> oidc
                .providerConfigurationEndpoint(
                        endpoint -> endpoint.providerConfigurationCustomizer(
                                configureOidcProvider()));
    }

    /**
     * Configures the OpenID Connect (OIDC) provider for the OAuth2 authorization server.
     *
     * @return The consumer to configure the OIDC provider.
     */
    private Consumer<OidcProviderConfiguration.Builder> configureOidcProvider(){

        /*configure the oidc provider*/
        return builder -> OidcProviderConfiguration.builder()
                /*set the oidc token signing algorithm*/
                .idTokenSigningAlgorithm(SignatureAlgorithm.ES512.name())
                /*set the allowed scopes*/
                .scopes(scopes -> scopes.addAll(List.of(OidcScopes.OPENID, OidcScopes.PROFILE)));
    }

    /**
     * Generates a new RSA KeyPair.
     *
     * @return KeyPair - the generated KeyPair
     */
    @NotNull
    private KeyPair generateKeyPair(){

        KeyPairGenerator keyPairGenerator = null;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    /**
     * Generates and returns a JWKSource<SecurityContext> implementation.
     *
     * @return JWKSource<SecurityContext> - the JWKSource<SecurityContext> implementation
     */
    @Bean
    public JWKSource<SecurityContext> jwkSource(){

        /*get the key pair*/
        KeyPair keyPair = generateKeyPair();
        /*get the public and private key from the key pair*/
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();

        /*create an RSA key*/
        RSAKey rsaKey = new RSAKey
                .Builder(publicKey)
                .privateKey(privateKey)
                .keyID(UUID.randomUUID().toString())
                .build();

        /*create a JWKSet of the rsa key*/
        JWKSet jwkSet = new JWKSet(rsaKey);
        /*create and return an implementation of JWKSource*/
        return new ImmutableJWKSet<>(jwkSet);
    }

    /**
     * Returns a JwtDecoder instance configured with the provided JWKSource.
     *
     * @param jwkSource The JWKSource implementation to be used by the JwtDecoder.
     * @return JwtDecoder - the configured JwtDecoder instance.
     */
    @Bean
    public JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource){

        /*get the jwt decoder for the jwk source*/
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    /**
     * Retrieves the configuration for the Authorization Server.
     *
     * @return AuthorizationServerSettings - the Authorization Server configuration
     */
    @Bean
    public AuthorizationServerSettings authorizationServerSettings(){

        return AuthorizationServerSettings
                .builder()
                .build();
    }

    /**
     * Creates an instance of OAuth2AuthorizationService using JdbcOperations and RegisteredClientRepository.
     *
     * @return OAuth2AuthorizationService - an instance of OAuth2AuthorizationService
     */
    @Bean
    public OAuth2AuthorizationService oAuth2AuthorizationService( ){
        /*create a JdbcOperations instance*/
        JdbcOperations jdbcClient = new JdbcTemplate(dataSource);
        /*create the service and return*/
        return new JdbcOAuth2AuthorizationService(jdbcClient, repository);
    }

    /**
     * Creates an instance of OAuth2AuthorizationConsentService using JdbcOperations and RegisteredClientRepository.
     *
     * @return OAuth2AuthorizationConsentService - an instance of OAuth2AuthorizationConsentService
     */
    @Bean
    public OAuth2AuthorizationConsentService oAuth2AuthorizationConsentService(){

        /*create a JdbcOperations instance*/
        JdbcOperations jdbcClient = new JdbcTemplate(dataSource);
        return new JdbcOAuth2AuthorizationConsentService(jdbcClient, repository);
    }

}
