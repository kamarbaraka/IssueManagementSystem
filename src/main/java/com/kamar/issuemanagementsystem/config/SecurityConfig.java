package com.kamar.issuemanagementsystem.config;

import com.kamar.issuemanagementsystem.app_properties.CompanyProperties;
import com.kamar.issuemanagementsystem.authority.repository.UserAuthorityRepository;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.cors.AppCorsConfigurationSourceImpl;
import com.kamar.issuemanagementsystem.external_resouces.service.EmailService;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import com.kamar.issuemanagementsystem.user.service.UserManagementServiceImpl;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.SessionManagementConfigurer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

/**
 * the security configuration.
 * @author kamar baraka.*/

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity,
                                                   @Qualifier("customAuthenticationEntryPoint")
                                                   AuthenticationEntryPoint entryPoint,
                                                   @Qualifier("appCorsConfigurationSourceImpl")
                                                   AppCorsConfigurationSourceImpl corsConfigurationSource
                                                   ) throws Exception{

        /*configure authentication method*/
        httpSecurity.httpBasic(httpBasic -> {
            httpBasic.realmName("IMS");
            httpBasic.authenticationEntryPoint(entryPoint);
        });

        /*configure session management*/
        httpSecurity.sessionManagement(session -> {
            session.maximumSessions(1);
            session.sessionFixation(
                    SessionManagementConfigurer.SessionFixationConfigurer::newSession
                    );
        });

//        httpSecurity.cors(cors -> cors.configurationSource(corsConfigurationSource));

        /*configure csrf*/
        httpSecurity.csrf(AbstractHttpConfigurer::disable);

        return httpSecurity.build();

    }


    @Bean
    public UserDetailsService userDetailsService(UserRepository userRepository,
                                                 EmailService emailService,
                                                 CompanyProperties companyProperties,
                                                 UserAuthorityUtility userAuthorityUtility){

        return new UserManagementServiceImpl(userRepository, emailService, companyProperties, userAuthorityUtility);

    }

    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder,
                                                         UserDetailsService userDetailsService){

        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);

        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){

        /*implement password encoder*/
        return new BCryptPasswordEncoder();
    }
}
