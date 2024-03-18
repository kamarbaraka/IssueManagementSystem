package com.kamar.issuemanagementsystem.global_configuration;


import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.oauth2.server.resource.OAuth2ResourceServerConfigurer;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.web.SecurityFilterChain;

/**
 * This class is a configuration class that configures the resource server for OAuth2 authentication.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
@Configuration
@RequiredArgsConstructor
public class GlobalOAuth2ResourceServerConfiguration {

    private final JwtDecoder jwtDecoder;
    private final AuthorizationServerSettings serverSettings;

    /**
     * Configures the resource server for OAuth2 authentication.
     *
     * @param httpSecurity the HttpSecurity object to configure the resource server
     * @return the configured SecurityFilterChain
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    @Order(3)
    public SecurityFilterChain oauth2ResourceServerFilterChain(@NotNull HttpSecurity httpSecurity) throws Exception{

        /*configure the resource server*/
        return httpSecurity.oauth2ResourceServer(Customizer.withDefaults())
                .oauth2ResourceServer(this.resourceServerConfiguration())
                .build();

    }


    /**
     * Returns a Customizer that configures the resource server for OAuth2 authentication.
     *
     * @return a Customizer object that configures the resource server
     */
    private Customizer<OAuth2ResourceServerConfigurer<HttpSecurity>> resourceServerConfiguration(){

        return resourceServer ->
            resourceServer.jwt(
                    jwt ->
                        jwt.decoder(jwtDecoder)
                                .jwkSetUri(serverSettings.getJwkSetEndpoint())

            );
    }
}
