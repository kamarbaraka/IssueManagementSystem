package com.kamar.issuemanagementsystem.config;

import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * code to initialize the application.
 * @author kamar baraka.*/

@Configuration
@RequiredArgsConstructor
public class AppInitConfig {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    public CommandLineRunner commandLineRunner(){

        return args -> {

            /*check if exists*/
            if (userRepository.findUserByUsername("admin@admin.com").isPresent()) {
                return;
            }
            /*create a user*/
            User user = new User();
            user.setUsername("admin@admin.com");
            user.setPassword(passwordEncoder.encode("admin"));
            user.setEnabled(true);
            user.setAuthority(Authority.ADMIN);
            user.setAccountNonExpired(true);
            user.setAccountNonLocked(true);
            user.setCredentialsNonExpired(true);

            /*persist the user*/
            userRepository.save(user);
        };
    }
}
