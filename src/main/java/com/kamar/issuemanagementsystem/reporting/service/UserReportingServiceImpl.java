package com.kamar.issuemanagementsystem.reporting.service;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import com.kamar.issuemanagementsystem.user_management.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * implementation of the user reporting service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Log4j2
public class UserReportingServiceImpl implements UserReportingService {

    private final UserEntityRepository userEntityRepository;
    private final UserAuthorityUtility userAuthorityUtility;
    private final DepartmentRepository departmentRepository;

    @Override
    public List<UserEntity> getEmployeesNotInDept() {

        /*get all employees*/
        UserAuthority employeeAuthority = userAuthorityUtility.getFor("employee");
        List<UserEntity> employees = userEntityRepository.findUserByAuthoritiesContaining(employeeAuthority);
        return employees.stream().filter(user -> departmentRepository.findDepartmentByMembersContaining(user)
                .isEmpty()).toList();
    }
}
