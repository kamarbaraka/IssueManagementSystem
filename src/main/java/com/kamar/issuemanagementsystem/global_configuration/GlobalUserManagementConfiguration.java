package com.kamar.issuemanagementsystem.global_configuration;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.*;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.session.*;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for global user management settings.
 * This class configures the security filters, session management, CSRF protection, and CORS.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class GlobalUserManagementConfiguration {

    /**
     * Configures the security filter chain for the HttpSecurity object.
     *
     * @param httpSecurity the HttpSecurity object to configure
     * @return the configured SecurityFilterChain object
     * @throws Exception if an error occurs during the configuration
     */
    @Bean
    @Order(2)
    public SecurityFilterChain userManagementFilterChain(@NotNull HttpSecurity httpSecurity) throws Exception{

        /*configure form login*/
        httpSecurity.formLogin(Customizer.withDefaults())
                .formLogin(this.configureFormLogin());

        /*configure session management*/
        httpSecurity.sessionManagement(this.configureSessionManagement());

        /*configure csrf*/
        httpSecurity.csrf(Customizer.withDefaults())
                .csrf(this.configureCsrf());

        /*configure cors*/
        httpSecurity.cors(Customizer.withDefaults())
                .cors(this.configureCors());

        return httpSecurity.build();

    }

    /**
     * Configures Cross-Origin Resource Sharing (CORS) for the HttpSecurity object.
     *
     * @return a Customizer object that configures the CORS settings.
     */
    private Customizer<CorsConfigurer<HttpSecurity>> configureCors(){

        return cors -> cors.configurationSource(
                new CorsConfigurationSource(){
                    /**
                     * Retrieves the CORS (Cross-Origin Resource Sharing) configuration for the given HTTP servlet request.
                     *
                     * @param request the HTTP servlet request
                     * @return the CORS configuration
                     */
                    @Override
                    public CorsConfiguration getCorsConfiguration( HttpServletRequest request) {

                        /*create a cors configuration*/
                        CorsConfiguration corsConfiguration = new CorsConfiguration();
                        corsConfiguration.addAllowedOrigin("*");
                        corsConfiguration.addAllowedMethod("*");
                        corsConfiguration.addAllowedHeader("*");
                        corsConfiguration.setAllowCredentials(true);

                        /*return*/
                        return corsConfiguration;
                    }
                }
        );
    }

    /**
     * Configures the Cross-Site Request Forgery (CSRF) protection for the HttpSecurity object.
     *
     * @return a Customizer object that configures the CSRF protection by ignoring the specified request matchers.
     * @since 1.0
     */
    private Customizer<CsrfConfigurer<HttpSecurity>> configureCsrf(){

        return csrf -> csrf.ignoringRequestMatchers("/api/**", "swagger-ui/**");
    }

    /**
     * Configures the session management for the HttpSecurity object.
     * @return a Customizer object that configures the session management settings.
     * @since 1.0
     */
    private Customizer<SessionManagementConfigurer<HttpSecurity>> configureSessionManagement(){
        String loginUrl = "/login";
        return session -> {
            session.maximumSessions(1)
                    .expiredUrl(loginUrl)
                    .sessionRegistry(new SessionRegistryImpl());
            session.sessionFixation(SessionManagementConfigurer.SessionFixationConfigurer::newSession)
                    .invalidSessionUrl(loginUrl)
                    .invalidSessionStrategy(new SimpleRedirectInvalidSessionStrategy(loginUrl))
                    .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED);
        };
    }

    /**
     * Configures the form login for the HttpSecurity object.
     *
     * @return a Customizer object that configures the form login settings.
     * @since 1.0
     */
    private Customizer<FormLoginConfigurer<HttpSecurity>> configureFormLogin(){

        return formLogin -> formLogin.loginPage("/login")
                .permitAll();
    }


    /**
     * Creates and returns a PasswordEncoder object.
     *
     * @return a PasswordEncoder object.
     */
    @Bean
    public PasswordEncoder passwordEncoder(){

        /*map the encoders*/
        Map<String, PasswordEncoder> passwordEncoders = new HashMap<>();
        /*add available encoders*/
        passwordEncoders.put("plainText", new PlainTextPasswordEncoder());
        passwordEncoders.put("bCrypt", new BCryptPasswordEncoder());
        passwordEncoders.put(null, new PlainTextPasswordEncoder());
        /*implement password encoder*/
        return new DelegatingPasswordEncoder("bCrypt", passwordEncoders);
    }
}

/**
 * Implementation of PasswordEncoder that encodes a password in plain text format.
 *
 * @author <a href="https://github.com/kamarbaraka">samson baraka</a>.
 */
class PlainTextPasswordEncoder implements PasswordEncoder{
    /**
     * Encodes a raw password.
     *
     * @param rawPassword the raw password to be encoded
     * @return the encoded password
     */
    @Override
    public String encode(CharSequence rawPassword) {

        /*convert to string and return*/
        return rawPassword.toString();
    }

    /**
     * Checks if a given raw password matches an encoded password.
     *
     * @param rawPassword     the raw password to be checked
     * @param encodedPassword the encoded password to be compared against
     * @return true if the raw password matches the encoded password, false otherwise
     */
    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        /*since its plain text, no encoding required*/
        return rawPassword.toString().equals(encodedPassword);
    }
}
