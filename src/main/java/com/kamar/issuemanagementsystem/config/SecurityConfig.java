package com.kamar.issuemanagementsystem.config;

import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import com.kamar.issuemanagementsystem.user.service.UserManagementServiceImpl;
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
import org.springframework.security.web.SecurityFilterChain;


import javax.sql.DataSource;

/**
 * the security configuration.
 * @author kamar baraka.*/

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{

        /*configure session management*/
        httpSecurity.sessionManagement(session -> {
            session.maximumSessions(2);
            session.sessionFixation(
                    SessionManagementConfigurer.SessionFixationConfigurer::changeSessionId);
            session.sessionConcurrency(concurrency -> {
                concurrency.maximumSessions(1);
                concurrency.maxSessionsPreventsLogin(true);
            });
        });

        /*configure login form*/
        httpSecurity.formLogin(login -> {});
        /*configure the logout*/
        httpSecurity.logout(logout -> {});
        /*configure csrf*/
        httpSecurity.csrf(csrf -> csrf.disable());



        return httpSecurity.build();

    }

    @Bean
    public UserDetailsService userDetailsService(DataSource dataSource, UserRepository userRepository){

        return new UserManagementServiceImpl(userRepository);

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
