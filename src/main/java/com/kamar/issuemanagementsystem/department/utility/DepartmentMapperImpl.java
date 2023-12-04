package com.kamar.issuemanagementsystem.department.utility;

import com.kamar.issuemanagementsystem.authority.entity.UserAuthority;
import com.kamar.issuemanagementsystem.authority.utility.UserAuthorityUtility;
import com.kamar.issuemanagementsystem.department.data.DepartmentCreationDto;
import com.kamar.issuemanagementsystem.department.data.DepartmentDto;
import com.kamar.issuemanagementsystem.department.entity.Department;
import com.kamar.issuemanagementsystem.department.exception.DepartmentException;
import com.kamar.issuemanagementsystem.user.entity.User;
import com.kamar.issuemanagementsystem.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * implementation of the department mapper contract.
 * @author kamar baraka.*/

@Service
@RequiredArgsConstructor
public class DepartmentMapperImpl implements DepartmentMapper {
    private final UserRepository userRepository;
    private final UserAuthorityUtility userAuthorityUtility;

    @Override
    public Department mapToDepartment(DepartmentCreationDto departmentCreationDto)throws DepartmentException {

        /*perform the mapping*/
        Department department = new Department();
        /*set the department name*/
        department.setDepartmentName(departmentCreationDto.departmentName());
        /*set the department email*/
        department.setDepartmentEmail(departmentCreationDto.email());

        /*check if the head of department exists and set the head of department*/
        User hOD = userRepository.findUserByUsername(departmentCreationDto.headOfDepartment()).orElseThrow(
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
                department.getMembers().stream().map(User::getUsername).toList()
        );
    }
}
