package com.kamar.issuemanagementsystem.innitialization;

import com.kamar.issuemanagementsystem.app_properties.InnitUserProperties;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.rating.entity.DepartmentPerformanceRating;
import com.kamar.issuemanagementsystem.rating.entity.UserRating;
import com.kamar.issuemanagementsystem.rating.repository.DepartmentPerformanceRatingRepository;
import com.kamar.issuemanagementsystem.rating.repository.UserRatingRepository;
import com.kamar.issuemanagementsystem.user.data.Authority;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * code to initialize the application.
 * @author kamar baraka.*/

@Component
public class AppInitConfig {

    @Bean
    public InitializingBean initializingBean(
            final UserRepository userRepository,
            final PasswordEncoder passwordEncoder,
            final UserRatingRepository userRatingRepository,
            final DepartmentRepository departmentRepository,
            final InnitUserProperties innitUserProperties,
            final DepartmentPerformanceRatingRepository departmentPerformanceRatingRepository
            ){

        return () -> {

            String username = innitUserProperties.username();
            /*check if exists*/
            if (userRepository.existsByUsername(username)) {
                return ;
            }

            /*create a user*/
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(innitUserProperties.password()));
            user.setAuthority(Authority.OWNER);
            user.setEnabled(true);
//            user.getUserRating().setRatingFor(user);

            /*persist the rating and user*/
            userRepository.save(user);

            /*create a department*/
            Department department = new Department();
            department.setDepartmentName(innitUserProperties.departmentName());
            department.setHeadOfDepartment(user);
            department.getMembers().add(user);
//            department.getPerformanceRating().setDepartment(department);

            /*persist the rating and department*/
            departmentRepository.save(department);
        };
    }
}
