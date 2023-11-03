package com.kamar.issuemanagementsystem.department.utility;

import com.kamar.issuemanagementsystem.department.data.DepartmentCreationDto;
import com.kamar.issuemanagementsystem.department.data.DepartmentDto;
import com.kamar.issuemanagementsystem.department.entity.Department;

/**
 * the department mapper contract.
 * @author kamar baraka.*/

public interface DepartmentMapper {

    Department mapToDepartment(DepartmentCreationDto departmentCreationDto);
    DepartmentDto mapToDto(Department department);
}
