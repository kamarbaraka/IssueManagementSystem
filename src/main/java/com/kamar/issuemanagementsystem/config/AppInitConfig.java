package com.kamar.issuemanagementsystem.config;

import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.rating.entity.Rating;
import com.kamar.issuemanagementsystem.rating.repository.RatingRepository;
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
    private final RatingRepository ratingRepository;
    private final DepartmentRepository departmentRepository;

    @Bean
    public CommandLineRunner commandLineRunner(){

        return args -> {

            String username = "kamar254baraka@gmail.com.com";
            /*check if exists*/
            if (userRepository.existsByUsername(username)) {
                return ;
            }

            /*create a user*/
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode("admin"));
            user.setAuthority(Authority.OWNER);
            user.setEnabled(true);

            /*persist the rating and user*/
            Rating userRating = user.getRating();
            userRating.setRatingFor(username);
            ratingRepository.save(userRating);
            userRepository.save(user);

            /*create a department*/
            Department department = new Department();
            department.setDepartmentName("INIT");
            department.setHeadOfDepartment(user);
            department.getMembers().add(user);

            /*persist the rating and department*/
            ratingRepository.save(department.getRating());
            departmentRepository.save(department);
        };
    }
}
