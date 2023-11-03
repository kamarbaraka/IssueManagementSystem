package com.kamar.issuemanagementsystem.department.service;

import com.kamar.issuemanagementsystem.department.data.DepartmentCreationDto;
import com.kamar.issuemanagementsystem.department.data.DepartmentDto;
import com.kamar.issuemanagementsystem.department.data.DepartmentDtoType;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.exception.DepartmentException;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.department.utility.DepartmentMapper;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Optional;

/**
 * implementation of the department management service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class DepartmentManagementServiceImpl implements DepartmentManagementService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final UserRepository userRepository;

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public void createDepartment(DepartmentCreationDto departmentCreationDto) {

        /*create the department*/
        Department department = departmentMapper.mapToDepartment(departmentCreationDto);
        /*persist the department*/
        departmentRepository.save(department);

    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public DepartmentDtoType getDepartmentByName(String name) throws DepartmentException {

        /*get the department by name*/
        return departmentRepository.findDepartmentByDepartmentName(name).orElseThrow(
                () -> new DepartmentException("no such department")
        );
    }

    @Override
    @Transactional
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public void addUsersToDepartment(String departmentName, String... usernames) throws DepartmentException {

        /*get and add the users to the department*/
        Department department = departmentRepository.findById(departmentName).orElseThrow(
                () -> new DepartmentException("no such department"));

        Arrays.stream(usernames).map(userRepository::findUserByUsername)
                .map(Optional::orElseThrow).forEach(user -> department.getMembers().add(user));
    }

}
