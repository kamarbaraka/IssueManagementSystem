package com.kamar.issuemanagementsystem.department.service;

import com.kamar.issuemanagementsystem.department.data.AddUserToDepartmentDTO;
import com.kamar.issuemanagementsystem.department.data.DepartmentCreationDto;
import com.kamar.issuemanagementsystem.department.data.DepartmentDto;
import com.kamar.issuemanagementsystem.department.data.DepartmentDtoType;
import com.kamar.issuemanagementsystem.department.exception.DepartmentException;

import java.util.List;

/**
 * the department management service contract.
 * @author kamar baraka.*/

public interface DepartmentManagementService {

    void createDepartment(DepartmentCreationDto departmentCreationDto) throws DepartmentException;
    DepartmentDtoType getDepartmentByName(String name)throws DepartmentException;

    void addUsersToDepartment(AddUserToDepartmentDTO addUserToDepartmentDTO) throws DepartmentException;
}
