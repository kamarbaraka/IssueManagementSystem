package com.kamar.issuemanagementsystem.department.utility;

import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.department.data.DepartmentCreationDto;
import com.kamar.issuemanagementsystem.department.data.DepartmentDto;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.exception.DepartmentException;
import com.kamar.issuemanagementsystem.department.repository.DepartmentRepository;
import com.kamar.issuemanagementsystem.user_management.entity.UserEntity;
import com.kamar.issuemanagementsystem.user_management.repository.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * implementation of the department mapper contract.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class DepartmentMapperImpl implements DepartmentMapper {
    private final UserEntityRepository userEntityRepository;
    private final UserAuthorityUtility userAuthorityUtility;
    private final DepartmentRepository departmentRepository;

    @Override
    public Department mapToDepartment(DepartmentCreationDto departmentCreationDto)throws DepartmentException {

        /*check if the department exists*/
        Optional<Department> optDept = departmentRepository.findDepartmentByDepartmentName(departmentCreationDto.departmentName());
        if (optDept.isPresent()) {
            /*throw*/
            throw new DepartmentException("department already exists");
        }
        /*perform the mapping*/
        Department department = new Department();
        /*set the department name*/
        department.setDepartmentName(departmentCreationDto.departmentName());
        /*set the department email*/
        department.setDepartmentEmail(departmentCreationDto.email());

        /*check if the head of department exists and set the head of department*/
        UserEntity hOD = userEntityRepository.findUserByUsername(departmentCreationDto.headOfDepartment()).orElseThrow(
                () -> new DepartmentException("HOD don't exist")
        );
        department.setHeadOfDepartment(hOD);
        /*set the authority of the HOD*/
        hOD.getAuthorities().add(userAuthorityUtility.getFor("department_admin"));
        /*add the hod as the member of the department*/
        department.getMembers().add(hOD);
        return department;
    }

    @Override
    public DepartmentDto mapToDto(Department department) {

        /*map*/
        return new DepartmentDto(
                department.getDepartmentName(),
                department.getDepartmentEmail(),
                department.getHeadOfDepartment().getUsername(),
                department.getPerformanceRating().getRating(),
                department.getMembers().stream().map(UserEntity::getUsername).toList()
        );
    }
}
