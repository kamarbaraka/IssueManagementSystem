package com.kamar.issuemanagementsystem.reporting.service;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.Serial;
import java.util.List;

/**
 * implementation of the user reporting service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Log4j2
public class UserReportingServiceImpl implements UserReportingService {

    private final UserRepository userRepository;
    private final UserAuthorityUtility userAuthorityUtility;
    private final DepartmentRepository departmentRepository;

    @Override
    public List<User> getEmployeesNotInDept() {

        /*get all employees*/
        UserAuthority employeeAuthority = userAuthorityUtility.getFor("employee");
        List<User> employees = userRepository.findUserByAuthoritiesContaining(employeeAuthority);
        return employees.stream().filter(user -> departmentRepository.findDepartmentByMembersContaining(user)
                .isEmpty()).toList();
    }
}
