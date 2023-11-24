package com.kamar.issuemanagementsystem.department.service;

import com.kamar.issuemanagementsystem.department.data.AddUserToDepartmentDTO;
import com.kamar.issuemanagementsystem.department.data.DepartmentCreationDto;
import com.kamar.issuemanagementsystem.department.data.DepartmentDto;
import com.kamar.issuemanagementsystem.department.data.DepartmentDtoType;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.exception.DepartmentException;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.department.utility.DepartmentMapper;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import com.kamar.issuemanagementsystem.user.utility.util.UserUtilityService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * implementation of the department management service.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentManagementServiceImpl implements DepartmentManagementService {

    private final DepartmentRepository departmentRepository;
    private final DepartmentMapper departmentMapper;
    private final UserRepository userRepository;
    private final UserUtilityService userUtilityService;

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public void createDepartment(DepartmentCreationDto departmentCreationDto)throws DepartmentException {

        /*create the department*/
        Department department = departmentMapper.mapToDepartment(departmentCreationDto);
        /*persist the department*/
        departmentRepository.save(department);

    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER', 'DEPARTMENT_ADMIN')")
    public DepartmentDtoType getDepartmentByName(String name) throws DepartmentException {

        UserDetails authenticatedUser = (UserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findUserByUsername(authenticatedUser.getUsername()).orElseThrow();

        /*get the department by name*/
        Department department = departmentRepository.findDepartmentByDepartmentName(name).orElseThrow(
                () -> new DepartmentException("no such department")
        );

        /*filter for department admin*/
        if (userUtilityService.hasAuthority(authenticatedUser, "department_admin")) {
            Department userDepartment = departmentRepository.findDepartmentByMembersContaining(user).orElseThrow();
            return department.equals(userDepartment)? departmentMapper.mapToDto(department) : null;
        }

        return departmentMapper.mapToDto(department);
    }

    @Override
    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    public void addUsersToDepartment(AddUserToDepartmentDTO addUserToDepartmentDTO) throws DepartmentException {

        /*get and add the users to the department*/
        Department department = departmentRepository.findById(
                addUserToDepartmentDTO.department()
        ).orElseThrow(
                () -> new DepartmentException("no such department"));

        addUserToDepartmentDTO.username().stream().map(userRepository::findUserByUsername)
                .map(Optional::orElseThrow).forEach(user -> department.getMembers().add(user));
    }

    @PreAuthorize("hasAnyAuthority('ADMIN', 'OWNER')")
    @Override
    public List<DepartmentDto> getAllDepartments(){

        /*get all departments*/
        List<Department> departments = departmentRepository.findAll();
        /*map to dto*/
        return departments.stream().map(departmentMapper::mapToDto).toList();
    }

}
