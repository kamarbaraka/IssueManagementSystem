package com.kamar.issuemanagementsystem.innitialization;

import com.kamar.issuemanagementsystem.app_properties.InnitUserProperties;
import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.service.UserAuthorityManagementService;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.rating.repository.DepartmentPerformanceRatingRepository;
import com.kamar.issuemanagementsystem.rating.repository.UserRatingRepository;
import com.kamar.issuemanagementsystem.ticket.entity.Sequences;
import com.kamar.issuemanagementsystem.ticket.repository.SequenceRepository;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.security.SecureRandom;
import java.util.UUID;

/**
 * code to initialize the application.
 * @author kamar baraka.*/

@Component
@Transactional
public class AppInitConfig {

    @Bean
    public InitializingBean initializingBean(
            final UserRepository userRepository,
            final PasswordEncoder passwordEncoder,
            final DepartmentRepository departmentRepository,
            final InnitUserProperties innitUserProperties,
            final UserAuthorityManagementService userAuthorityManagementService,
            final SequenceRepository sequenceRepository
            ){

        return () -> {

            /*check if innit user exists*/
            String username = innitUserProperties.username();
            if (userRepository.existsByUsername(username)) {
                return ;
            }


            /*create ticket id generator*/
            Sequences sequences = new Sequences();
            sequenceRepository.save(sequences);

            /*create a roles*/
            UserAuthority ownerAuthority = userAuthorityManagementService.createAuthority("owner");
            userAuthorityManagementService.createAuthority("user");
            userAuthorityManagementService.createAuthority("admin");
            userAuthorityManagementService.createAuthority("employee");
            userAuthorityManagementService.createAuthority("department_admin");


            /*create a user*/
            User user = new User();
            user.setUsername(username);
            user.setPassword(passwordEncoder.encode(innitUserProperties.password()));
            user.getAuthorities().add(ownerAuthority);
            user.setEnabled(true);

            /*persist the rating and user*/
            userRepository.save(user);

            /*create a department*/
            Department department = new Department();
            department.setDepartmentName(innitUserProperties.departmentName());
            department.setDepartmentEmail(innitUserProperties.departmentEmail());
            department.setHeadOfDepartment(user);
            department.getMembers().add(user);

            /*persist the rating and department*/
            departmentRepository.save(department);
        };
    }
}
