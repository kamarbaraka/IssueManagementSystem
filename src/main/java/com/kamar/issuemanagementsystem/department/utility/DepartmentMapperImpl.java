package com.kamar.issuemanagementsystem.department.utility;

import com.kamar.issuemanagementsystem.department.data.DepartmentCreationDto;
import com.kamar.issuemanagementsystem.department.data.DepartmentDto;
import com.kamar.issuemanagementsystem.department.entity.Department;
import org.springframework.stereotype.Service;

/**
 * implementation of the department mapper contract.
 * @author kamar baraka.*/

@Service
public class DepartmentMapperImpl implements DepartmentMapper {
    @Override
    public Department mapToDepartment(DepartmentCreationDto departmentCreationDto) {

        /*perform the mapping*/
        Department department = new Department();
        department.setDepartmentName(departmentCreationDto.departmentName());
        return department;
    }

    @Override
    public DepartmentDto mapToDto(Department department) {
        return null;
    }
}
