package com.kamar.issuemanagementsystem.department.data;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

/**
 * the department creation dto.
 * @author kamar baraka.*/

public record DepartmentCreationDto(

        @Size(min = 2, max = 50, message = "department name too long")
        String departmentName,
        @Email
        String email,
        @Email
        String headOfDepartment
) implements DepartmentDtoType{
}
